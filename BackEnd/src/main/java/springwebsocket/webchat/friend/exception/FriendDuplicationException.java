package springwebsocket.webchat.friend.exception;

import springwebsocket.webchat.error.ErrorCode;
import springwebsocket.webchat.error.exception.InvalidValueException;

public class FriendDuplicationException extends InvalidValueException {
    public FriendDuplicationException() {
        super(ErrorCode.FRIEND_ALREADT_TAKEN);
    }
}
