package kopo.poly.service.impl;

import kopo.poly.domain.Documents;
import kopo.poly.repository.DocumentRepository;
import kopo.poly.service.IEmbeddingClient;
import kopo.poly.service.IEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService implements IEmbeddingService {

    private final IEmbeddingClient embeddingClient;
    private final DocumentRepository documentRepository;

    @Override
    public List<Double> generateEmbedding(String text) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        // Feign Client를 통해 임베딩 API 호출
        Map<String, Object> response = embeddingClient.generateEmbedding(requestBody);

        // 응답에서 임베딩 벡터 추출
        return (List<Double>) response.get("embedding");
    }

    @Override
    public void saveDocument(String content) {
        List<Double> embedding = generateEmbedding(content);

        Documents documentEntry = new Documents();
        documentEntry.setContent(content);
        documentEntry.setEmbedding(embedding);
        documentRepository.save(documentEntry);
    }

    @Override
    public List<Documents> findSimilarDocuments(double[] questionEmbedding) {
        // 모든 문서 가져오기
        List<Documents> allDocuments = documentRepository.findAll();

        // 코사인 유사도 계산 및 필터링
        return allDocuments.stream()
                .map(document -> {
                    double similarity = calculateCosineSimilarity(
                            questionEmbedding,
                            document.getEmbedding().stream().mapToDouble(Double::doubleValue).toArray()
                    );
                    document.setSimilarityScore(similarity);
                    return document;
                })
                .filter(doc -> doc.getSimilarityScore() >= 0.4) // 유사도 임계값 설정
                .sorted((d1, d2) -> Double.compare(d2.getSimilarityScore(), d1.getSimilarityScore()))
                .limit(5) // 상위 5개 문서만 반환
                .collect(Collectors.toList());
    }



    @Override
    public double[] embedQuestion(String question) {
        List<Double> embeddingList = generateEmbedding(question);
        return embeddingList.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of same length");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
