package springwebsocket.webchat.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.springdata.SpringDataJpaFriendshipRepository;
import springwebsocket.webchat.repository.springdata.SpringDataJpaMemberRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static springwebsocket.webchat.entity.Friendship.FriendshipStatus.FRIENDS;

@Transactional
@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaFriendshipRepository implements FriendshipRepository {

    private final SpringDataJpaFriendshipRepository friendshipRepository;
    private final SpringDataJpaMemberRepository memberRepository;

    @Override
    public Friendship sendFriendRequest(Long senderId, String receiverEmail) {

        Optional<Member> senderMember = memberRepository.findById(senderId);
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
    public void acceptFriendRequestById(Long senderId, String receiverEmail) {

        Optional<Member> senderMember = memberRepository.findById(senderId);
        Optional<Member> receiverMember = memberRepository.findByEmail(receiverEmail);


        Member sender = senderMember.get();
        Member receiver = receiverMember.get();

        // 기존의 Friendship 엔터티를 찾는다.
        Optional<Friendship> existingFriendship = friendshipRepository.findByUserIdAndFriendId(sender, receiver);

        existingFriendship.ifPresent(friendship -> {
            // Friendship 엔터티의 상태를 FRIENDS로 update.
            friendship.setStatus(FRIENDS);
            // 업데이트된 Friendship 엔터티를 저장.
//            friendshipRepository.save(friendship);
        });

    }


    @Override
    public void rejectFriendRequestById(Long id, String Email) {

        Optional<Member> memberMe = memberRepository.findById(id);
        Optional<Member> memberYou = memberRepository.findByEmail(Email);

        // 기존의 Friendship 엔터티를 찾는다.
        Optional<Friendship> existingFriendship = friendshipRepository.findByUserIdAndFriendId(memberMe.get(), memberYou.get());

        existingFriendship.ifPresent(friendship ->{
            // Friendship 엔터티를 삭제
            friendshipRepository.delete(existingFriendship.get());
        });

    }

    @Override
    public List<Member> findByFriendIdAndStatus(Long id) {
        Optional<Member> userMember = memberRepository.findById(id);
        return friendshipRepository.findByFriendIdAndStatus(userMember.get(), Friendship.FriendshipStatus.PENDING);
    }

    @Override
    public List<Long> findByUserIdAndStatusOrFriendIdAndStatus(Long userId) {

        List<Long> friendList = new ArrayList<>();

        Optional<Member> userMember = memberRepository.findById(userId);
        friendList.addAll(friendshipRepository.findFriendshipByUserIdAndStatus(userMember.get(), FRIENDS));
        friendList.addAll(friendshipRepository.findFriendshipsByFriendIdAndStatus(userMember.get(), FRIENDS));

        return friendList;

    }
}
