package springwebsocket.webchat.member.exception.response;

import org.springframework.http.HttpStatus;
import springwebsocket.webchat.error.ErrorCode;

public class ExceptionResponse {
    private final int status;
    private final String code;
    private final String message;

    public ExceptionResponse(ErrorCode exceptionCode) {
        this.status = exceptionCode.getHttpStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}