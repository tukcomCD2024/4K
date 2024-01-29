package springwebsocket.webchat.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaFriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserIdAndFriendId(Member sender, Member receiver);

    @Query("SELECT f.userId FROM Friendship f WHERE f.friendId = :friendId AND f.status = :status")
    List<Member> findByFriendIdAndStatus(@Param("friendId") Member friend, @Param("status") Friendship.FriendshipStatus status);

    @Query("SELECT f FROM Friendship f WHERE (f.userId = :userId OR f.friendId = :friendId) AND f.status = :status")
    List<Member> findByUserIdOrFriendIdAndStatus(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("status") Friendship.FriendshipStatus status);

}
