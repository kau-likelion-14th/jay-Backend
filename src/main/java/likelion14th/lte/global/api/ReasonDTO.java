package likelion14th.lte.global.api;

// 응답코드, 응답메세지를 하나루 묶어 응답할 수 있게 만든 DTO

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// API 응답 상세 정보
@Getter
@Builder
public class ReasonDTO implements BaseCode {
    // implements BascCode -> BaseCode 인터페이스를 구현하는 클래스
    // BaseCod를 구현하는 모든 클래스/enum은 반드시 ReaseonDTO를 반환해야됨

    private HttpStatus httpStatus; // HTTP 상태 코드
    private String code; // 응답 코드
    private String message; // 응답 메시지

    // 인터페이스 구현
    @Override
    //@Override → BaseCode에 선언만 되어 있는 getReason() 메서드를 ErrorCode SuccessCode에서 구현(재정의, override)
    public ReasonDTO getReason() {
        return this;
    }
    // this는 자기 자신을 반환

}