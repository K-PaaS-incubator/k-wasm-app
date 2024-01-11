package kr.or.kpass.kwasm.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.repository.FileRepository;
import kr.or.kpass.kwasm.repository.entity.FileEntity;
import kr.or.kpass.kwasm.service.IFileManageService;
import kr.or.kpass.kwasm.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileManageService implements IFileManageService {

    private final AmazonS3Client amazonS3;

    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    String homeDir = "/kwasm/data"; // 컴파일될 파일 임시 저장소

    /**
     * Object Storage 저장하기
     *
     * @param mf   파일 데이터
     * @param pDTO 저장될 파일정보
     * @return 저장된 파일경로
     */
    @Override
    public FileDTO uploadObjectStorage(MultipartFile mf, FileDTO pDTO) {

        log.info(this.getClass().getName() + " uploadObjectStorage Start!");

        // 저장할 이미지 URL
        String imageUrl;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(mf.getSize());
        objectMetadata.setContentType(mf.getContentType());

        try (InputStream inputStream = mf.getInputStream()) {

            amazonS3.putObject(new PutObjectRequest(bucket, pDTO.serverFileName(), inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imageUrl = amazonS3.getUrl(bucket, pDTO.serverFileName()).toString();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to File Upload!");
        }

        FileDTO rDTO = FileDTO.builder().serverFileUrl(imageUrl).build();

        log.info(this.getClass().getName() + " uploadObjectStorage End!");

        return rDTO;
    }

    /**
     * 파일시스템에 저장하기
     *
     * @param mf   파일 데이터
     * @param pDTO 저장될 파일정보
     * @return 저장된 파일경로
     */
    @Override
    public FileDTO uploadFileSystem(MultipartFile mf, FileDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + " uploadFileSystem Start!");

        // 저장될 폴더 생성 및 폴더 위치 가져오기
        String path = FileUtil.mkdirForDate(homeDir);
        log.info(path);

        // 파일 저장
        mf.transferTo(new File(path + "/" + pDTO.serverFileName()));

        if (new File(path + "/" + pDTO.serverFileName()).exists()) {
            log.info("File Upload Success!");

        }

        FileDTO rDTO = FileDTO.builder().saveFilePath(path)
                .saveFileName(pDTO.saveFileName())
                .ext(pDTO.ext()).serverFileName(pDTO.serverFileName()).build();

        log.info(this.getClass().getName() + " uploadFileSystem End!");

        return rDTO;
    }

    @Override
    public int deleteFileObjectStorage(String fileName) {

        int res = 0;
        try {
            amazonS3.deleteObject(bucket, fileName);
            res = 1;

        } catch (AmazonS3Exception e) {
            log.info("Error : " + e);

        } catch (SdkClientException e) {
            log.info("Error : " + e);

        }

        return res;
    }

    @Override
    @Transactional
    public FileDTO saveFileData(FileDTO pDTO) throws Exception {
        log.info(getClass().getName() + " Meta Data saveFileData Start!");

        FileEntity pEntity = FileEntity.builder()
                .orgFileName(pDTO.orgFileName())
                .saveFileName(pDTO.saveFileName())
                .saveFilePath(pDTO.saveFilePath())
                .ext(pDTO.ext())
                .serverFileName("")
                .serverFileUrl("")
                .regDt(pDTO.regDt())
                .build();

        // 데이터 저장 및 PK 값 가져오기
        Long fileSeq = fileRepository.save(pEntity).getFileSeq();
        log.info("fileSeq : " + fileSeq);

        // 결과값 구조 만들어주기
        FileDTO dto = FileDTO.builder().fileSeq(fileSeq).build();

        log.info(getClass().getName() + " Meta Data saveFileData End!");

        return dto;
    }

    /**
     * 컴파일된 WASM 결과 파일들 압축하기
     *
     * @param pDTO 저장될 파일정보
     * @return 생성된 압축 파일 정보
     */
    @Override
    public FileDTO compressionZipFileSystem(FileDTO pDTO) throws Exception {

        log.info(getClass().getName() + " compressionZip Start!");

        // 압축할 파일들이 저장된 디렉토리
        String dir = pDTO.saveFilePath();
        log.info("dir : " + dir);

        // 생성항 압축 파일명
        String zipFile = dir + "/" + pDTO.saveFileName() + ".zip";
        log.info("zipFile : " + zipFile);

        // 압축할 파일들
        String[] fileNames = new File(dir).list();

        FileOutputStream fos = new FileOutputStream(zipFile); // 생성할 Zip 파일

        ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
        for (String fileName : fileNames) {

            log.info("fileName :" + fileName);
            FileSystemResource fileSystemResource = new FileSystemResource(dir + "/" + fileName);
            ZipEntry zipEntry = new ZipEntry(fileSystemResource.getFilename());
            zipEntry.setSize(fileSystemResource.contentLength());
            zipEntry.setTime(System.currentTimeMillis());
            zipOutputStream.putNextEntry(zipEntry);
            StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();

        FileDTO rDTO = FileDTO.builder().saveFilePath(dir)
                .saveFileName(pDTO.saveFileName())
                .ext("zip").build();
        log.info(getClass().getName() + " compressionZip End!");

        return rDTO;
    }

    /**
     * 압축된 K-WASM 컴파일 결과 다운로드
     *
     * @param pDTO 저장될 파일정보
     * @return 생성된 압축 파일 정보
     */
    @Override
    public FileDTO downloadWasmFileSystem(FileDTO pDTO) throws Exception {

        log.info(getClass().getName() + " downloadWasmFileSystem Start!");

        Optional<FileEntity> rEntity = fileRepository.findByFileSeq(pDTO.fileSeq());

        FileDTO rDTO = new ObjectMapper().convertValue(rEntity.orElseThrow(), FileDTO.class);

        log.info(getClass().getName() + " downloadWasmFileSystem End!");

        return rDTO;
    }
}
