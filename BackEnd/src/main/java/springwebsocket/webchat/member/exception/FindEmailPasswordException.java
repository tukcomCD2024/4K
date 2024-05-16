package springwebsocket.webchat.member.exception;

import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;

public class FindEmailPasswordException extends BusinessException {
    public FindEmailPasswordException() {
        super(ErrorCode.FIND_TAKEN);
    }
}
