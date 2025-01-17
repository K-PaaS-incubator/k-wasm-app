package kopo.poly.dto;

import lombok.Builder;

@Builder
public record RagDTO(String role, String question, String message) {

}
