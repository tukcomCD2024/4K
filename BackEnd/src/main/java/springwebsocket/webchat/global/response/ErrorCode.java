package springwebsocket.webchat.global.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // -----예외(Http 상태, 메시지)------ //

    // Member
    EMAIL_ALREADY_TAKEN(400, "이미 존재하는 이메일입니다."),
    LOGIN_FAIL_TAKEN(400,"로그인 실패"),
    FIND_TARGETUSER_TAKEN(400,"상대방이 존재하지 않습니다."),

    //Friend
    FRIEND_ALREADT_TAKEN(400,"이미 친구 요청을 보낸 상대입니다.");



    private final int status;
    private final String message;

}
