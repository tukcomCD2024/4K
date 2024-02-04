package springwebsocket.webchat.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.member.repository.JpaMemberRepository;
import springwebsocket.webchat.member.repository.MemberRepository;
import springwebsocket.webchat.member.service.MemberService;
import springwebsocket.webchat.member.service.MemberServiceV1;


@Configuration
public class MemberConfig {

    private final EntityManager em;

    public MemberConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService userService() {
        return new MemberServiceV1(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

}
