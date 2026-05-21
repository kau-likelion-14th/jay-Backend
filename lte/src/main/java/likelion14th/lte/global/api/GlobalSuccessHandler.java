package likelion14th.lte.global.api;

// 컨트롤러의 응답이 클라이언트에게 가기 직전에 가로채서, 실제 HTTP 응답 헤더의 상태 코드를 세팅해주는 역할
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// 성공 응답에 대해 ApiResponse 안의 HttpStatus를 실제 HTTP Status로 반영하는 핸들러
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalSuccessHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 바디가 ApiResponse인 경우에만 beforeBodyWrite에서 처리
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 1. 응답 바디가 ApiResponse 타입이고,
        // 2. HTTP 응답 객체가 ServletServerHttpResponse 타입일 때
        if (body instanceof ApiResponse<?> apiResponse &&
                response instanceof ServletServerHttpResponse servletResponse) {

            // ApiResponse 객체 안에 저장된 HttpStatus가 존재한다면
            if (apiResponse.getHttpStatus() != null) {
                // 실제 HTTP 응답 헤더의 상태 코드를 해당 HttpStatus로 덮어씌기
                servletResponse.setStatusCode(apiResponse.getHttpStatus());
            }
        }
        // 바디 데이터(JSON 내용) 자체는 건드리지 않고 그대로 반환
        return body;
    }
}