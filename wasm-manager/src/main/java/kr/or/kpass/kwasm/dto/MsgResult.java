package kr.or.kpass.kwasm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 실행 결과 메시지 관리
 */
@Getter
@AllArgsConstructor
public enum MsgResult {

    Success(true, 1, "성공했습니다."),
    SaveSuccess(true, 1, "저장이 성공했습니다."),
    Fail(false, 0, "실패했습니다.");

    private final boolean status; // 성공, 실패 여부
    private final int code; // 상태코드
    private final String msg; // 실행 결과에 따른 메시지

}
