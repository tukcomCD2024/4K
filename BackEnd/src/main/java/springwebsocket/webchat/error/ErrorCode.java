package springwebsocket.webchat.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    // -----예외(Http 상태, 에러코드, 메시지)------ //

    // Member
    EMAIL_ALREADY_TAKEN("이미 존재하는 이메일입니다."),

    //Friend
    FRIEND_ALREADT_TAKEN("이미 친구 요청을 보낸 상대입니다.");



    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }
}
