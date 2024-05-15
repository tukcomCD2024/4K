package springwebsocket.webchat.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springwebsocket.webchat.global.jwt.TokenProvider;
import springwebsocket.webchat.member.repository.JpaMemberRepository;
import springwebsocket.webchat.member.repository.MemberRepository;
import springwebsocket.webchat.member.repository.RefreshMemberRepository;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;
import springwebsocket.webchat.member.service.MemberServiceImpl;
import springwebsocket.webchat.member.service.MemberService;


@Configuration
@RequiredArgsConstructor
public class MemberConfig {

    private final SpringDataJpaMemberRepository jpaMemberRepository;

    private final EntityManager em;

    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;

    private final TokenProvider tokenProvider;

    private final RefreshMemberRepository refreshMemberRepository;


    @Bean
    public MemberService userService() {
        return new MemberServiceImpl(jpaMemberRepository, memberRepository(), encoder(), tokenProvider,refreshMemberRepository);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em, springDataJpaMemberRepository);
    }

}
