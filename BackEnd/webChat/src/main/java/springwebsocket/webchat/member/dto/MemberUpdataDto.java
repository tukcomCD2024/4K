package springwebsocket.webchat.member.dto;


import lombok.Data;

@Data
public class MemberUpdataDto {

    private String email;
    private String password;
    private String name;


    public MemberUpdataDto() {
    }

    public MemberUpdataDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
