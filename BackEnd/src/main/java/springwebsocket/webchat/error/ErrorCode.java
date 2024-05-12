package springwebsocket.webchat.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    // -----예외(Http 상태, 에러코드, 메시지)------ //

    // Member
    EMAIL_ALREADY_TAKEN(400, "M001", "이미 존재하는 이메일입니다."),
//    USER_NAME_NOT_FOUND(400, "M004", "해당 이름의 사용자를 찾을수 없습니다."),
//    EMAIL_USER_NOT_FOUND(400, "M005", "해당 이메일의 사용자가 존재하지 않습니다."),
//    EMAIL_EMPTY(400, "M007", "이메일을 입력해주세요."),


    //Friend
    FRIEND_ALREADT_TAKEN(400, "M011", "이미 친구 요청을 보낸 상대입니다.");
//    INVALID_LOGIN_INFO(400, "M009", "이메일 또는 비밀번호를 다시 확인해 주세요."),
//    UNAUTHORIZED_USER(401, "M010", "로그인 후 이용가능합니다."),
//    USER_EMPTY(400, "M011", "존재하지 않는 사용자입니다."),
//    LOGIN_AGAIN(400, "M012", "로그인을 다시 시도해 주세요");
//



    private final int httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final int httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
