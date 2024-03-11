package springwebsocket.webchat.member.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final String name;
    private final String email;

    public UserResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

