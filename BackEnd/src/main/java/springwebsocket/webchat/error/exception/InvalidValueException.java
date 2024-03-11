package springwebsocket.webchat.error.exception;

import springwebsocket.webchat.error.ErrorCode;

public class InvalidValueException extends ApplicationException{

    protected InvalidValueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
