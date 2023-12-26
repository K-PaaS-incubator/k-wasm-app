package kr.or.kpass.kwasm.dto;

import lombok.Builder;

@Builder
public record FileDTO(
        String fileSeq, // File Table SEQ
        String orgFileName, // 원래 사용자 파일 이름
        String serverFileName, // 서버에 저장된 파일 이름
        String serverFileUrl, // 서버에 저장된 파일 URL
        String ext// 파일 확장자
) {
}
