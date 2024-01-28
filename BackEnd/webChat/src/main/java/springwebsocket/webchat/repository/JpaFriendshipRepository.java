package springwebsocket.webchat.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.springdata.SpringDataJpaFriendshipRepository;
import springwebsocket.webchat.repository.springdata.SpringDataJpaMemberRepository;

import java.util.Optional;

@Transactional
@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaFriendshipRepository implements FriendshipRepository{

    private final SpringDataJpaFriendshipRepository friendshipRepository;
    private final SpringDataJpaMemberRepository memberRepository;

    @Override
    public Friendship sendFriendRequest(Long senderId, Long receiverId) {

        // Friendship 엔터티 생성
        Friendship friendship = new Friendship();

        /**
         * A -> B 친구 요청 시나리오
         * 1. memberRepository에서 receiverId가 있는지 확인
         * 2. A -> B status PENDING
         * 3. B -> A statue RECEIVED
         * 이러면 B는 요청 목록에서 열거형 RECEIVED 로 select가능
         */
        Optional<Member> friendId = memberRepository.findById(receiverId);

        // senderId와 receiverId 설정
        Member sender = new Member();
        sender.setId(senderId);
        Member receiver = new Member();
        receiver.setId(receiverId);

        // A -> B status PENDING
        Friendship friendshipAB = new Friendship();
        friendshipAB.setUser(sender);
        friendshipAB.setFriend(receiver);
        friendshipAB.setStatus(Friendship.FriendshipStatus.PENDING);
        friendshipRepository.save(friendshipAB);

        //  B -> A status RECEIVED
        Friendship friendshipBA = new Friendship();
        friendshipBA.setUser(receiver);
        friendshipBA.setFriend(sender);
        friendshipBA.setStatus(Friendship.FriendshipStatus.RECEIVED);
        friendshipRepository.save(friendshipBA);

        return friendshipAB;
    }

    @Override
    public void acceptFriendRequestById(Long id) {

    }

    @Override
    public void rejectFriendRequestById(Long id) {

    }

    @Override
    public Optional<Friendship> findByFriendIdAndStatus(Long userId, Friendship.FriendshipStatus status) {
        return null;
    }

    @Override
    public Optional<Friendship> findByUserIdAndStatus(Long userId, Friendship.FriendshipStatus status) {
        return null;
    }

    @Override
    public Optional<Friendship> findByUserIdAndStatusOrFriendIdAndStatus(Long userId, Friendship.FriendshipStatus status1, Long friendId, Friendship.FriendshipStatus status2) {
        return null;
    }
}
