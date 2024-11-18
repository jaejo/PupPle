package jajo.jajo_ex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public static <T> ResponseDto<T> fail(String code, String msg) {
        return new ResponseDto<>(false, null, new Error(code, msg));
    }
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }
    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String msg;
    }
}
