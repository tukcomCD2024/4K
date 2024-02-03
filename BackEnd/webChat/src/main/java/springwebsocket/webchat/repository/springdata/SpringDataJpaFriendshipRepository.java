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

    Optional<Friendship> findByFriendIdAndUserId(Member sender, Member receiver);

    @Query("SELECT f.userId FROM Friendship f WHERE f.friendId = :friendId AND f.status = :status")
    List<Member> findByFriendIdAndStatus(@Param("friendId") Member friend, @Param("status") Friendship.FriendshipStatus status);

    @Query("SELECT f.friendId.email FROM Friendship f WHERE f.userId = :userId AND f.status= :status")
    List<String> findFriendshipByUserIdAndStatus(Member userId,Friendship.FriendshipStatus status);

    @Query("SELECT f.userId.email FROM Friendship f WHERE f.friendId = :friendId AND f.status= :status")
    List<String> findFriendshipsByFriendIdAndStatus(Member friendId,Friendship.FriendshipStatus status);
}
