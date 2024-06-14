package springwebsocket.webchat.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum Naming Format : {행위}_{목적어}_{성공여부} message format: 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ResultCode {
	// member
	UPDATE_MEMBER_SUCCESS(200,"멤버 업데이트 성공"),

	;
	private final int status;
	private final String message;
}
