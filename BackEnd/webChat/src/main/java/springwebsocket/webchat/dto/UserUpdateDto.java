package springwebsocket.webchat.dto;


import lombok.Data;

@Data
public class UserUpdateDto {

    private String email;
    private String password;
    private String name;


    public UserUpdateDto() {
    }

    public UserUpdateDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
