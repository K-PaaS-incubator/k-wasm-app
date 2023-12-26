package kr.or.kpass.kwasm.service;

import kr.or.kpass.kwasm.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {

    // 파일 업로드 로직
    String upload(MultipartFile mf, String fileName) throws Exception;

    // 업로드 파일 삭제 로직
    int fileDelete(String fileName) throws Exception;

    // 파일 정보 저장
    int saveFileData(FileDTO pDTO) throws Exception;
}
