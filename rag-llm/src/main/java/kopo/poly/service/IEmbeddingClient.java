package kopo.poly.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "EmbeddingClient", url = "${embedding.api.url}")
public interface IEmbeddingClient {

    /**
     * 임베딩 생성 API 호출 메서드
     *
     * @param requestBody 요청 본문 (text 필드 포함)
     * @return 응답 데이터 (embedding 필드 포함)
     */
    @PostMapping("/embedding")
    Map<String, Object> generateEmbedding(@RequestBody Map<String, String> requestBody);
}
