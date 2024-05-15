package springwebsocket.webchat.friend.exception;

import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;

public class FriendDuplicationException extends BusinessException {
    public FriendDuplicationException() {
        super(ErrorCode.FRIEND_ALREADT_TAKEN);
    }
}
