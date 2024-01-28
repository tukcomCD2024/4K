package springwebsocket.webchat.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Friendship {

    public enum FriendshipStatus {
        PENDING,    // 친구 요청 보냄
        ACCEPTED,   // 친구 요청 수락됨
        REJECTED,   // 친구 요청 거절됨
        FRIENDS,    // 서로 친구
        // 추가적인 상태들을 필요에 따라 정의할 수 있음
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Member friend;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}