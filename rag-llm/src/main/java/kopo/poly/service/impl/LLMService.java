package kopo.poly.service.impl;

import kopo.poly.service.ILLMService;
import kopo.poly.service.ILlamaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LLMService implements ILLMService {

    private final ILlamaClient llamaClient;

    /**
     * 질문과 관련 문서를 기반으로 LLM 응답을 생성합니다.
     *
     * @param question  사용자 질문
     * @param documents 관련 문서 목록
     * @return LLM의 응답
     */
    @Override
    public String generateResponse(String question, List<String> documents) {
        // 시스템 메시지 및 사용자 질문 구성
        String systemMessage = "You are a helpful assistant.";
        String userMessage = "Question: " + question;

        if (documents != null && !documents.isEmpty()) {
            userMessage += "\nRelevant documents:\n" + String.join("\n", documents);
        }

        log.info("Generated system message: {}", systemMessage);
        log.info("Generated user message: {}", userMessage);

        // 요청 본문 생성
        Map<String, Object> request = new HashMap<>();
        request.put("messages", List.of(
                Map.of("role", "system", "content", systemMessage),
                Map.of("role", "user", "content", userMessage)
        ));

        log.info("Request payload: {}", request);

        // Llama API 호출
        Map<String, Object> response;
        try {
            response = llamaClient.chatCompletion(request);
        } catch (Exception e) {
            log.error("Error occurred while calling Llama API", e);
            return "An error occurred while generating a response.";
        }

        // 응답 처리
        Map<String, Object> choice = (Map<String, Object>) ((List<?>) response.get("choices")).get(0);
        String result = (String) ((Map<String, Object>) choice.get("message")).get("content");

        log.info("LLM Response: {}", result);

        return result.trim();
    }
}
