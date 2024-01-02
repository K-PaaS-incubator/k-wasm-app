package kr.or.kpass.kwasm.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.repository.FileRepository;
import kr.or.kpass.kwasm.repository.entity.FileEntity;
import kr.or.kpass.kwasm.service.IFileUploadService;
import kr.or.kpass.kwasm.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService implements IFileUploadService {

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
        mf.transferTo(new File(path + pDTO.serverFileName()));

        FileDTO rDTO = FileDTO.builder().saveFilePath(path).serverFileName(pDTO.serverFileName()).build();

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
    public int saveFileData(FileDTO pDTO) throws Exception {
        log.info(getClass().getName() + " Meta Data saveFileData Start!");

        int res;

        FileEntity pEntity = FileEntity.builder()
                .orgFileName(pDTO.orgFileName())
                .serverFileName(pDTO.serverFileName())
                .serverFileUrl(pDTO.serverFileUrl())
                .ext(pDTO.ext())
                .build();

        fileRepository.save(pEntity);

        res = 1;

        log.info(getClass().getName() + " Meta Data saveFileData End!");

        return res;
    }

}
