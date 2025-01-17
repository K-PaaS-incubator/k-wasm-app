package kopo.poly.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "LlamaClient", url = "${llama.api.url}")
public interface ILlamaClient {

    @PostMapping("/v1/chat/completions")
    Map<String, Object> chatCompletion(@RequestBody Map<String, Object> request);
}
