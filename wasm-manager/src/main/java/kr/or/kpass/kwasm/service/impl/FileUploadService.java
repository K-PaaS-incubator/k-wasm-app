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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
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

    @Override
    public String upload(MultipartFile mf, String fileName) throws Exception {
        log.info(this.getClass().getName() + " Object Storage Upload Start!");


        // 저장할 이미지 URL
        String imageUrl;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(mf.getSize());
        objectMetadata.setContentType(mf.getContentType());

        try (InputStream inputStream = mf.getInputStream()) {

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imageUrl = amazonS3.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        log.info(this.getClass().getName() + " Object Storage Upload End!");

        return imageUrl;
    }

    @Override
    public int fileDelete(String fileName) throws Exception {

        int res = 0;
        try {
            amazonS3.deleteObject(bucket, fileName);
            res = 1;

        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
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
