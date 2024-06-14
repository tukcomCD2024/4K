package springwebsocket.webchat.member.exception;

import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;

import static springwebsocket.webchat.global.response.ErrorCode.FIND_TARGETUSER_TAKEN;

public class FindTargetException extends BusinessException {
    public FindTargetException() {
        super(FIND_TARGETUSER_TAKEN);
    }
}
