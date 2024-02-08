package springwebsocket.webchat.member.exception;

import springwebsocket.webchat.error.ErrorCode;
import springwebsocket.webchat.error.exception.InvalidValueException;

public class EmailDuplicatedException extends InvalidValueException {

    public EmailDuplicatedException(String email) {
        super(email, ErrorCode.EMAIL_ALREADY_TAKEN);
    }

}
