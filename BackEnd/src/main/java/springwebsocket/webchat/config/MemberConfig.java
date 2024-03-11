package springwebsocket.webchat.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.member.repository.JpaMemberRepository;
import springwebsocket.webchat.member.repository.MemberRepository;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;
import springwebsocket.webchat.member.service.MemberService;
import springwebsocket.webchat.member.service.MemberServiceV1;
import springwebsocket.webchat.member.service.MemberServiceV2;
import springwebsocket.webchat.member.service.MemberServiceV3;


@Configuration
@RequiredArgsConstructor
public class MemberConfig {

    private final EntityManager em;

    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;

    @Bean
    public MemberServiceV3 userService() {
        return new MemberServiceV2(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em, springDataJpaMemberRepository);
    }

}
