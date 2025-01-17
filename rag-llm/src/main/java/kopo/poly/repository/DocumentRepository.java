package kopo.poly.repository;

import kopo.poly.domain.Documents;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Documents, String> {

//    /**
//     * MongoDB의 $nearSphere를 사용하여 유사도 기반 검색을 수행합니다.
//     *
//     * @param embedding 질문 임베딩 벡터
//     * @return 유사 문서 리스트
//     */
//    @Query("{ 'embedding': { $nearSphere: ?0 } }")
//    List<Documents> findSimilarDocuments(double[] embedding);
}
