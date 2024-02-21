package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest
@WebAppConfiguration
class JpaUserRepositoryTest {


    @Qualifier("memberRepository")
    @Autowired
    MemberRepository userRepository;

    @Test
    void save() {
        //given
        Member user = new Member("rkdwlsgur", "1234", "강진혁");

        //when
        Member saveUser = userRepository.save(user);


        //then
        Member findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser).isEqualTo(saveUser);
    }

    @Test
    void updateUser() {
        //given
        Member user = new Member("user1@naver.com", "1234", "user1");
        Member saveUser = userRepository.save(user);
        Long userId = saveUser.getId();

        //when
        MemberUpdataDto updateParam = new MemberUpdataDto("user2@naver.com", "1234", "user2");
        userRepository.update(userId, updateParam);

        //then
        Member findUser = userRepository.findById(userId).get();
        assertThat(findUser.getEmail()).isEqualTo(updateParam.getEmail());
        assertThat(findUser.getPassword()).isEqualTo(updateParam.getPassword());
        assertThat(findUser.getName()).isEqualTo(updateParam.getName());

    }

    @Test
    void delete() {
        //given
        Member user = new Member("user1@naver.com", "1234", "user1");
        Member saveUser = userRepository.save(user);
        Long userId = saveUser.getId();

        //when
        userRepository.delete(userId);

        //then
        assertThat(userRepository.findById(userId)).isEmpty();

    }

    // 이메일 존재하는 경우
    @Test
    void findByLoginEmail_WhenUserExists_ShouldReturnUser() {
        // given
        Member user = new Member("test@example.com", "password", "Test User");
        userRepository.save(user);

        // when
        Optional<Member> result = userRepository.findByLoginEmail("test@example.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    // 이메일 존재하지 않은경우
    @Test
    void findByLoginEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        // when
        Optional<Member> result = userRepository.findByLoginEmail("nonexistent@example.com");

        // then
        assertThat(result).isEmpty();
    }
}