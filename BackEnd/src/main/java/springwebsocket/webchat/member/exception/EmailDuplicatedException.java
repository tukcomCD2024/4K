package springwebsocket.webchat.member.exception;

import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;

public class EmailDuplicatedException extends BusinessException {

    public EmailDuplicatedException(String email) {
        super(email, ErrorCode.EMAIL_ALREADY_TAKEN);
    }

}
