package springwebsocket.webchat.friend.entity;

import jakarta.persistence.*;
import lombok.Data;
import springwebsocket.webchat.member.entity.Member;


@Data
@Entity
@Table(name = "friendship",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "friend_id"})
        })
public class Friendship {

    public enum FriendshipStatus {
        PENDING,    // 친구 요청
        FRIENDS,    // 서로 친구
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Member userId;

    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id", nullable = false)
    private Member friendId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}