package springwebsocket.webchat.member.exception;

import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;

import static springwebsocket.webchat.global.response.ErrorCode.LOGIN_FAIL_TAKEN;

public class LoginFailException extends BusinessException {

    public LoginFailException() {
        super(LOGIN_FAIL_TAKEN);
    }
}
