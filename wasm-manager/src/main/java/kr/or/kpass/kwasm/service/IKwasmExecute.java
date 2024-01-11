package kr.or.kpass.kwasm.service;

import kr.or.kpass.kwasm.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IKwasmExecute {

    /**
     * K-WASM 컴파일러 실행을 위한 함수
     *
     * @param pDTO 컴파일할 파일 정보
     */
    FileDTO doExecuteKwasm(FileDTO pDTO, MultipartFile mf) throws Exception;

}
