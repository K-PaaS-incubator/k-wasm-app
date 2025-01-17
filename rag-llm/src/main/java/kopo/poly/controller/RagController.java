package kopo.poly.controller;

import kopo.poly.domain.Documents;
import kopo.poly.dto.RagDTO;
import kopo.poly.service.IEmbeddingService;
import kopo.poly.service.impl.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = "/rag")
@CrossOrigin(origins = "*", methods = RequestMethod.POST)
@RequiredArgsConstructor
@RestController
public class RagController {

    private final IEmbeddingService embeddingService;
    private final LLMService llmService;

    @PostMapping("/generate")
    public ResponseEntity<RagDTO> generateAnswer(@RequestBody String question) {
        log.info("generate Start!");

        // Step 1: 질문 임베딩 생성
        double[] questionEmbedding = embeddingService.embedQuestion(question);

        // Step 2: 벡터 DB에서 유사 문서 검색
        List<Documents> relevantDocuments = embeddingService.findSimilarDocuments(questionEmbedding);

        log.info("Relevant documents: {}", relevantDocuments);

        // Step 3: 검색된 문서에서 내용을 추출
        List<String> documentContents = relevantDocuments.stream()
                .map(Documents::getContent)
                .collect(Collectors.toList());

        log.info("Document contents: {}", documentContents);

        // Step 4: 유사 문서가 존재할 경우 프롬프트를 생성하고 응답 생성
        if (!documentContents.isEmpty()) {
            String prompt = generatePrompt(question, documentContents);
            String combinedResponse = llmService.generateResponse(prompt, documentContents);
            log.info("Response from LLM with combined prompt: {}", combinedResponse);
            return ResponseEntity.ok(RagDTO.builder().role("K-LLM").question(question).message(combinedResponse).build());
        }

        // Step 5: 유사 문서가 없는 경우 LLM 호출로 응답 생성
        String llmAnswer = llmService.generateResponse(question, List.of());
        log.info("Response from LLM: {}", llmAnswer);

        log.info("generate End!");
        return ResponseEntity.ok(RagDTO.builder().role("K-LLM").question(question).message(llmAnswer).build());
    }

    /**
     * 질문과 유사 문서를 기반으로 프롬프트를 생성합니다.
     *
     * @param question         사용자의 질문
     * @param documentContents 유사 문서 내용 리스트
     * @return 생성된 프롬프트
     */
    private String generatePrompt(String question, List<String> documentContents) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음은 사용자의 질문과 관련성이 높은 문서입니다. ");
        prompt.append("이 문서와 질문을 바탕으로 정확하고 간결하게 답변해 주세요.\n\n");

        // 질문 포함
        prompt.append("질문: ").append(question).append("\n\n");

        // 유사 문서 포함
        for (int i = 0; i < documentContents.size(); i++) {
            prompt.append("문서 ").append(i + 1).append(": ").append(documentContents.get(i)).append("\n\n");
        }

        prompt.append("답변:");
        return prompt.toString();
    }

    @PostMapping("/document")
    public ResponseEntity<Void> saveDocument(@RequestBody String content) {
        embeddingService.saveDocument(content);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-documents")
    public ResponseEntity<Void> saveBatchDocuments(@RequestBody List<String> contents) {
        contents.forEach(embeddingService::saveDocument);
        return ResponseEntity.ok().build();
    }
}
