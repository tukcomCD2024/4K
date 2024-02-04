package springwebsocket.webchat.friend.entity;

import jakarta.persistence.*;
import lombok.Data;
import springwebsocket.webchat.member.entity.Member;


@Data
@Entity
public class Friendship {

    public enum FriendshipStatus {
        PENDING,    // 친구 요청
        FRIENDS,    // 서로 친구
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member userId;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Member friendId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}