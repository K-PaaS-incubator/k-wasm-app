package kopo.poly.service;

import kopo.poly.domain.Documents;

import java.util.List;

public interface IEmbeddingService {

    /**
     * 주어진 텍스트를 임베딩으로 변환합니다.
     *
     * @param text 텍스트 데이터
     * @return 임베딩 벡터 리스트
     */
    List<Double> generateEmbedding(String text);

    /**
     * 주어진 콘텐츠를 임베딩으로 변환한 후 MongoDB에 저장합니다.
     *
     * @param content 저장할 콘텐츠
     */
    void saveDocument(String content);

    /**
     * 질문 임베딩과 유사한 문서를 검색합니다.
     *
     * @param questionEmbedding 질문 임베딩 벡터
     * @return 유사 문서 리스트
     */
    List<Documents> findSimilarDocuments(double[] questionEmbedding);

    /**
     * 주어진 질문을 임베딩으로 변환합니다.
     *
     * @param question 질문 텍스트
     * @return 임베딩 벡터 배열
     */
    double[] embedQuestion(String question);
}
