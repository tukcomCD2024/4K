package springwebsocket.webchat.friend.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaFriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserIdAndFriendId(Member sender, Member receiver);

    Optional<Friendship> findByFriendIdAndUserId(Member sender, Member receiver);

    @Query("SELECT u.email AS email, u.name AS name, u.language AS language " +
            "FROM Friendship AS f " +
            "JOIN f.userId AS u " +
            "WHERE f.friendId = :friendId AND f.status = :status")
    List<UserInfoMapping> findByFriendIdAndStatus(@Param("friendId") Member friend, @Param("status") Friendship.FriendshipStatus status);


    @Query("SELECT u.email AS email, u.name AS name, u.language AS language " +
            "FROM Friendship AS f " +
            "JOIN f.friendId AS u " +
            "WHERE f.userId = :userId AND f.status= :status")
    List<UserInfoMapping> findFriendshipByUserIdAndStatus(Member userId,Friendship.FriendshipStatus status);

    @Query("SELECT u.email AS email, u.name AS name, u.language AS language " +
            "FROM Friendship AS f " +
            "JOIN f.userId AS u " +
            "WHERE f.friendId = :friendId AND f.status= :status")
    List<UserInfoMapping> findFriendshipsByFriendIdAndStatus(Member friendId,Friendship.FriendshipStatus status);


}
