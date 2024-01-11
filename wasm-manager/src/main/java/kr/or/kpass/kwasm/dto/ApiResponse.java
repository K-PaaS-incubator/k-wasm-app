package kr.or.kpass.kwasm.dto;

import lombok.Builder;

@Builder
public record ApiResponse<T>(boolean status, int code, String msg, T result) {

}
