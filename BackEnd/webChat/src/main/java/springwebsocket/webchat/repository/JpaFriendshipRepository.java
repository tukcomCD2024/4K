package springwebsocket.webchat.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.springdata.SpringDataJpaFriendshipRepository;
import springwebsocket.webchat.repository.springdata.SpringDataJpaMemberRepository;

import java.util.Optional;

@Transactional
@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaFriendshipRepository implements FriendshipRepository {

    private final SpringDataJpaFriendshipRepository friendshipRepository;
    private final SpringDataJpaMemberRepository memberRepository;

    @Override
    public Friendship sendFriendRequest(String senderEmail, String receiverEmail) {

        Optional<Member> senderMember = memberRepository.findByEmail(senderEmail);
        Optional<Member> receiverMember = memberRepository.findByEmail(receiverEmail);

        if (receiverMember.isPresent()) {
            // 상대방 Id가 존재할 때
            Member sender = senderMember.get();
            Member receiver = receiverMember.get();

            Friendship friendship = new Friendship();
            friendship.setUserId(sender);
            friendship.setFriendId(receiver);
            friendship.setStatus(Friendship.FriendshipStatus.PENDING);
            friendshipRepository.save(friendship);

            return friendship;
        } else {
            return null;
        }

    }

    @Override
    public void acceptFriendRequestById(String senderEmail, String receiverEmail) {

        Optional<Member> senderMember = memberRepository.findByEmail(senderEmail);
        Optional<Member> receiverMember = memberRepository.findByEmail(receiverEmail);


        Member sender = senderMember.get();
        Member receiver = receiverMember.get();

        // 기존의 Friendship 엔터티를 찾는다.
        Optional<Friendship> existingFriendship = friendshipRepository.findByUserIdAndFriendId(sender, receiver);

        existingFriendship.ifPresent(friendship -> {
            // Friendship 엔터티의 상태를 FRIENDS로 update.
            friendship.setStatus(Friendship.FriendshipStatus.FRIENDS);
            // 업데이트된 Friendship 엔터티를 저장.
            friendshipRepository.save(friendship);
        });

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
