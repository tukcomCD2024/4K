package springwebsocket.webchat.member.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import springwebsocket.webchat.chat.entity.MemberChattingRoom;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private String firebaseToken;
    private String language;
    private String role;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    public void createChatting(MemberChattingRoom memberChattingRoom) {
        memberChattingRooms.add(memberChattingRoom);
    }

    public Member(String email, String password, String name, String language, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.language = language;
        this.role = role;
    }

}
