package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.springdata.SpringDataJpaFriendshipRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static springwebsocket.webchat.entity.Friendship.FriendshipStatus.PENDING;


@Slf4j
@SpringBootTest
class JpaFriendshipRepositoryTest {

    @Autowired
    private JpaFriendshipRepository friendshipRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SpringDataJpaFriendshipRepository springDataJpaFriendshipRepository;


    @Test
    @Transactional
    public void sendFriendRequest_Success() {
        // given
        Member sender = new Member("sender@naver.com", "1234", "sender");
        Member receiver = new Member("receiver@naver.com", "1234", "receiver");

        // when
        memberRepository.save(sender);
        memberRepository.save(receiver);

        Friendship friendship = friendshipRepository.sendFriendRequest(sender.getId(), receiver.getEmail());



        //then
        assertThat(friendship.getUserId().getId()).isEqualTo(sender.getId());
        assertThat(friendship.getFriendId().getId()).isEqualTo(receiver.getId());
        assertThat(friendship.getStatus()).isEqualTo(PENDING);
    }


    @Test
    @Transactional
    public void sendFriendRequest_Fail() {
        // given
        Member sender = new Member("sender@naver.com", "1234", "sender");
        Member receiver = new Member("receiver@naver.com", "1234", "receiver");

        // when
        memberRepository.save(sender);

        Friendship friendship = friendshipRepository.sendFriendRequest(sender.getId(), receiver.getEmail());

        //then
        assertThat(friendship).isNull();

    }

    @Test
    @Transactional
    public void acceptFriendRequestById(){
        // given
        Member sender = new Member("sender@naver.com", "1234", "sender");
        Member receiver = new Member("receiver@naver.com", "1234", "receiver");
        memberRepository.save(sender);
        memberRepository.save(receiver);
        Friendship friendship = friendshipRepository.sendFriendRequest(sender.getId(), receiver.getEmail());

        // when
        friendshipRepository.acceptFriendRequestById(sender.getId(), receiver.getEmail());

        // then
        Optional<Friendship> byUserIdAndFriendId = springDataJpaFriendshipRepository.findByUserIdAndFriendId(sender,receiver);

        assertThat(byUserIdAndFriendId.get().getUserId().getId()).isEqualTo(sender.getId());
        assertThat(byUserIdAndFriendId.get().getFriendId().getId()).isEqualTo(receiver.getId());
        assertThat(byUserIdAndFriendId.get().getStatus()).isEqualTo(Friendship.FriendshipStatus.FRIENDS);

    }

    @Test
    @Transactional
    public void findByFriendIdAndStatus() {
        // given
        Member sender = new Member("sender@naver.com", "1234", "sender");
        Member receiver1 = new Member("receiver1@naver.com", "1234", "receiver1");
        Member receiver2 = new Member("receiver2@naver.com", "1234", "receiver2");
        Member senderMember = memberRepository.save(sender);
        Member receiverMember1 = memberRepository.save(receiver1);
        Member receiverMember2 = memberRepository.save(receiver2);

        //when

        /**
         * receiver1 -> sender 친구 요청 보냄
         * receiver2 -> sender 친구 요청 보냄
         * sender 는 친구목록에 2명이 있어야 함.
         */
        friendshipRepository.sendFriendRequest(receiver1.getId(), sender.getEmail());
        friendshipRepository.sendFriendRequest(receiver2.getId(), sender.getEmail());

        List<Member> friendList = friendshipRepository.findByFriendIdAndStatus(sender.getId());

        //then
        log.info("senderMember={}", senderMember.getId());
        log.info("receiver1={}",receiverMember1.getId());
        log.info("receiver2={}",receiverMember2.getId());
        log.info("friendList ={} ", friendList.get(0).toString());
        log.info("friendList ={} ", friendList.get(1).toString());
        log.info("friendList[0].getId = {}",friendList.get(0).getId());
        log.info("friendList[1].getId = {}",friendList.get(1).getId());


        assertThat(friendList).hasSize(2);
        assertThat(friendList)
                .extracting(Member::getId)
                .containsExactlyInAnyOrder(receiver1.getId(),receiver2.getId());
    }

    @Test
    @Transactional
    void findByUserIdAndStatusOrFriendIdAndStatus() {
        // given
        Member sender = new Member("sender@naver.com", "1234", "sender");
        Member receiver1 = new Member("receiver1@naver.com", "1234", "receiver1");
        Member receiver2 = new Member("receiver2@naver.com", "1234", "receiver2");

        // 멤버 save
        Member senderMember = memberRepository.save(sender);
        Member receiverMember1 = memberRepository.save(receiver1);
        Member receiverMember2 = memberRepository.save(receiver2);

        //친구 요청
        friendshipRepository.sendFriendRequest(sender.getId(), receiver1.getEmail());
        friendshipRepository.sendFriendRequest(receiver2.getId(), sender.getEmail());
        //친구 수락
        friendshipRepository.acceptFriendRequestById(sender.getId(), receiver1.getEmail());
        friendshipRepository.acceptFriendRequestById(receiver2.getId(), sender.getEmail());

//        //둘 다 친구 확인
//        Optional<Friendship> byUserIdAndFriendId = springDataJpaFriendshipRepository.findByUserIdAndFriendId(sender,receiver1);
//
//        log.info("byUserIdAndFriendId ={}",byUserIdAndFriendId.get().getStatus());

        // when

        List<Long> friendList = friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(sender.getId());

        // then
        log.info("senderMember={}", senderMember.getId());
        log.info("receiver1={}",receiverMember1.getId());
        log.info("receiver2={}",receiverMember2.getId());
        log.info("friendList[0] ={}",friendList.get(0));
        log.info("friendList[1] ={}",friendList.get(1));

        assertThat(friendList).hasSize(2);
        assertThat(friendList)
                .containsExactlyInAnyOrder(receiver1.getId(),receiver2.getId());
    }
}