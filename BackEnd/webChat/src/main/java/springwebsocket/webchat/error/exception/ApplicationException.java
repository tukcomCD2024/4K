package springwebsocket.webchat.error.exception;

import springwebsocket.webchat.error.ErrorCode;

public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}