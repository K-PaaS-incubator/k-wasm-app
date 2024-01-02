package kr.or.kpass.kwasm.service;

import kr.or.kpass.kwasm.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {

    /**
     * Object Storage 저장하기
     *
     * @param pDTO 저장될 파일정보
     * @param mf   파일 데이터
     * @return 저장된 파일경로
     */
    FileDTO uploadObjectStorage(MultipartFile mf, FileDTO pDTO) throws Exception;

    /**
     * 파일시스템에 저장하기
     *
     * @param pDTO 저장될 파일정보
     * @param mf   파일 데이터
     * @return 저장된 파일경로
     */
    FileDTO uploadFileSystem(MultipartFile mf, FileDTO pDTO) throws Exception;

    /**
     * Object Storage 저장된 파일 삭제
     *
     * @param fileName 삭제할 파일 이름
     * @return 수행 결과
     */
    int deleteFileObjectStorage(String fileName) throws Exception;

    /**
     * 저장된 파일 메타 정보를 RDBMS 저장하기
     */
    int saveFileData(FileDTO pDTO) throws Exception;
}
