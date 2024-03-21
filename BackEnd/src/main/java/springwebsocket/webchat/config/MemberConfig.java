package springwebsocket.webchat.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.member.repository.JpaMemberRepository;
import springwebsocket.webchat.member.repository.MemberRepository;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;
import springwebsocket.webchat.member.service.MemberServiceImpl;
import springwebsocket.webchat.member.service.MemberService;


@Configuration
@RequiredArgsConstructor
public class MemberConfig {

    private final EntityManager em;

    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;

    @Bean
    public MemberService userService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em, springDataJpaMemberRepository);
    }

}
