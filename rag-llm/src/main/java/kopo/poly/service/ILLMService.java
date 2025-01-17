package kopo.poly.service;

import java.util.List;

public interface ILLMService {

    /**
     * 질문과 관련 문서를 기반으로 LLM 응답을 생성합니다.
     *
     * @param question  사용자 질문
     * @param documents 관련 문서 목록
     * @return LLM의 응답
     */
    String generateResponse(String question, List<String> documents);
}
