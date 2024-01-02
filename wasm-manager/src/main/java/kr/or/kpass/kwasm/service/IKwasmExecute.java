package kr.or.kpass.kwasm.service;

import kr.or.kpass.kwasm.dto.FileDTO;

public interface IKwasmExecute {

    /**
     * K-WASM 컴파일러 실행을 위한 함수
     *
     * @param pDTO 컴파일할 파일 정보
     */
    int doExecuteKwasm(FileDTO pDTO) throws Exception;
}
