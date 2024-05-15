package springwebsocket.webchat.global.exception;

import org.springframework.http.HttpStatus;
import springwebsocket.webchat.error.ErrorCode;

public class ExceptionResponse {
    private final String message;

    public ExceptionResponse(ErrorCode exceptionCode) {
        this.message = exceptionCode.getMessage();
    }

    public String getMessage() {
        return message;
    }
}