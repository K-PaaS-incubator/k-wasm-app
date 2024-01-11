package kr.or.kpass.kwasm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
public record FileDTO(
        Long fileSeq, // File Table SEQ
        String orgFileName, // 원래 사용자 파일 이름
        String serverFileName, // Object Storage 저장된 파일 이름
        String serverFileUrl, // Object Storage 저장된 파일 URL
        String ext, // 파일 확장자
        String saveFilePath, // 파일시스템에 저장되는 폴더 경로
        String saveFileName, // 파일시스템에 저장되는 파일명(확장자 제외)
        String compileWasm, // 컴파일된 결과-WASM
        String compileJs, // 컴파일된 결과-JS
        String compileHtml, // 컴파일된 결과-HTML
        String regDt // 파일 생성일시
) {
}
