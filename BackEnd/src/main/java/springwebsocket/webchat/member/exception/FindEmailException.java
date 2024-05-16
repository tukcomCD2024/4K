package springwebsocket.webchat.member.exception;

import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;

public class FindEmailException extends BusinessException {
    public FindEmailException() {
        super(ErrorCode.FIND_TAKEN);
    }
}
