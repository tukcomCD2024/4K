package springwebsocket.webchat.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;

import java.util.Optional;

public interface SpringDataJpaFriendshipRepository extends JpaRepository<Friendship,Long> {
    Optional<Friendship> findByUserIdAndFriendId(Member sender, Member receiver);

}
