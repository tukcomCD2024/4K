package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.springdata.SpringDataJpaFriendshipRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static springwebsocket.webchat.entity.Friendship.FriendshipStatus.FRIENDS;
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

        Friendship friendship = friendshipRepository.sendFriendRequest(sender.getEmail(), receiver.getEmail());



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

        Friendship friendship = friendshipRepository.sendFriendRequest(sender.getEmail(), receiver.getEmail());

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
        Friendship friendship = friendshipRepository.sendFriendRequest(sender.getEmail(), receiver.getEmail());

        // when
        friendshipRepository.acceptFriendRequestById(sender.getEmail(), receiver.getEmail());

        // then
        Optional<Friendship> byUserIdAndFriendId = springDataJpaFriendshipRepository.findByUserIdAndFriendId(sender,receiver);

        assertThat(byUserIdAndFriendId.get().getUserId().getId()).isEqualTo(sender.getId());
        assertThat(byUserIdAndFriendId.get().getFriendId().getId()).isEqualTo(receiver.getId());
        assertThat(byUserIdAndFriendId.get().getStatus()).isEqualTo(Friendship.FriendshipStatus.FRIENDS);

    }
}