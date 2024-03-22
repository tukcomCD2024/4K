package springwebsocket.webchat.global.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.global.jwt.dto.CustomUserDetails;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.repository.MemberRepository;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SpringDataJpaMemberRepository memberRepository;

    public CustomUserDetailsService(SpringDataJpaMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> userData = memberRepository.findByEmail(email);

        if (userData.isPresent()) {

            return new CustomUserDetails(userData);
        }
        return null;
    }
}
