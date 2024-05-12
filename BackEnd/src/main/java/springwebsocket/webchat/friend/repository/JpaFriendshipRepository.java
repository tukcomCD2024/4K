package springwebsocket.webchat.friend.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.friend.dto.response.friendMessageResponse;
import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.friend.exception.FriendDuplicationException;
import springwebsocket.webchat.friend.repository.springdata.UserInfoMapping;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.friend.repository.springdata.SpringDataJpaFriendshipRepository;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springwebsocket.webchat.friend.entity.Friendship.FriendshipStatus.FRIENDS;

@Transactional
@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaFriendshipRepository implements FriendshipRepository {

    private final SpringDataJpaFriendshipRepository friendshipRepository;
    private final SpringDataJpaMemberRepository memberRepository;

    @Override
    public ResponseEntity<?> sendFriendRequest(String senderEmail, String receiverEmail) {

        Optional<Member> senderMember = memberRepository.findByEmail(senderEmail);
        Optional<Member> receiverMember = memberRepository.findByEmail(receiverEmail);

        if (receiverMember.isPresent()) {
            // 상대방 Email가 존재할 때
            Member sender = senderMember.get();
            Member receiver = receiverMember.get();

            Friendship friendship = new Friendship();
            friendship.setUserId(sender);
            friendship.setFriendId(receiver);
            friendship.setStatus(Friendship.FriendshipStatus.PENDING);

            try {
                friendshipRepository.save(friendship);
            } catch (DataIntegrityViolationException ex) {
                // DataIntegrityViolationException으로 수정
                throw new FriendDuplicationException();
            }
            friendMessageResponse message = new friendMessageResponse("success");
            return ResponseEntity.ok().body(message);

        } else {
            friendMessageResponse message = new friendMessageResponse("not exist");
            return ResponseEntity.ok().body(message);
        }

    }

    @Override
    public String acceptFriendRequestById(String senderEmail, String receiverEmail) {

        Optional<Member> senderMember = memberRepository.findByEmail(senderEmail);
        Optional<Member> receiverMember = memberRepository.findByEmail(receiverEmail);


        Member sender = senderMember.get();
        Member receiver = receiverMember.get();

        // 기존의 Friendship 엔터티를 찾는다.
        Optional<Friendship> existingFriendship = friendshipRepository.findByFriendIdAndUserId(sender, receiver);

        if (existingFriendship.isEmpty()) return "fail";

        existingFriendship.ifPresent(friendship -> {
            // Friendship 엔터티의 상태를 FRIENDS로 update.
            friendship.setStatus(FRIENDS);
            // 업데이트된 Friendship 엔터티를 저장.
//            friendshipRepository.save(friendship);
        });
        return "success";
    }


    @Override
    public String rejectFriendRequestById(String senderEmail, String receiverEmail) {

        Optional<Member> memberMe = memberRepository.findByEmail(senderEmail);
        Optional<Member> memberYou = memberRepository.findByEmail(receiverEmail);

        // 기존의 Friendship 엔터티를 찾는다.
        Optional<Friendship> existingFriendship = friendshipRepository.findByFriendIdAndUserId(memberMe.get(), memberYou.get());

        if (existingFriendship.isEmpty()) return "fail";

        existingFriendship.ifPresent(friendship -> {
            // Friendship 엔터티를 삭제
            friendshipRepository.delete(existingFriendship.get());
        });

        return "success";
    }

    @Override
    public List<UserInfoMapping> findByFriendIdAndStatus(Long id) {
        Optional<Member> userMember = memberRepository.findById(id);
        return friendshipRepository.findByFriendIdAndStatus(userMember.get(), Friendship.FriendshipStatus.PENDING);
    }

    @Override
    public List<UserInfoMapping> findByUserIdAndStatusOrFriendIdAndStatus(Long userId) {

        List<UserInfoMapping> friendList = new ArrayList<>();

        Optional<Member> userMember = memberRepository.findById(userId);

        List<UserInfoMapping> friendList1 = friendshipRepository.findFriendshipByUserIdAndStatus(userMember.get(), FRIENDS);
        List<UserInfoMapping> friendList2 = friendshipRepository.findFriendshipsByFriendIdAndStatus(userMember.get(), FRIENDS);

        friendList.addAll(friendList1);
        friendList.addAll(friendList2);

        return friendList;

    }
}
