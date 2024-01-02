package kr.or.kpass.kwasm.service;

import kr.or.kpass.kwasm.dto.FileDTO;

/**
 * EMSCRIPTEN 기반 컴파일
 */
public interface IKwasmCompile {

    /**
     * WASM 컴파일 수행하기
     *
     * @param pDTO 파일정보
     * @return 컴파일 성공여부
     */
    int doCompile(FileDTO pDTO) throws Exception;
}
