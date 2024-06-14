package springwebsocket.webchat.member.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdataDto {

    private String email;
    private String password;
    private String newPassword;
    private String name;
    private String language;

    public MemberUpdataDto(String email, String password, String newPassword, String name, String language) {
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.name = name;
        this.language = language;
    }
}
