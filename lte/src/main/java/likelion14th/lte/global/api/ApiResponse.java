package likelion14th.lte.global.api;

// 프로젝트 전체에서 프론트로 반환될 표준화된 응답(JSON) 포맷을 정의하는 껍데기 클래스

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> { // API 응답

    private final Boolean isSuccess; // 성공 여부
    private final String code; // 응답 코드
    private final String message; // 메세지
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result; // 응답 데이터

    // HTTP 상태 코드는 바디에는 포함하지 않고, 성공 핸들러에서만 사용
    @JsonIgnore
    private final HttpStatus httpStatus;

    //성공 - 바디 + HTTP Status 정보까지 포함
    public static <T> ApiResponse<T> onSuccess(BaseCode code, T result) {
        return new ApiResponse<>(
                true,
                code.getReason().getCode(),
                code.getReason().getMessage(),
                result,
                code.getReason().getHttpStatus()
        );
    }

    //실패 1 - 에러 메셎만 반환할 때 (데이터 없음)
    public static ApiResponse<Void> onFailure(BaseCode code) {
        return new ApiResponse<>(
                false,
                code.getReason().getCode(),
                code.getReason().getMessage(),
                null,
                code.getReason().getHttpStatus()
        );
    }

    //실패 2 - 에러와 함께 상세 데이터를 반환할 때
    public static <T> ApiResponse<T> onFailure(BaseCode code, T data) {
        return new ApiResponse<>(
                false,
                code.getReason().getCode(),
                code.getReason().getMessage(),
                data,
                code.getReason().getHttpStatus()
        );
    }
}