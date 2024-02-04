package springwebsocket.webchat.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceV1 implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member save(Member user) {
        return memberRepository.save(user);
    }

    @Override
    public void update(Long userId, MemberUpdataDto updateParam) {
        memberRepository.update(userId, updateParam);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        memberRepository.delete(id);
    }

    public String login(String loginEmail, String password) {
        Member user = memberRepository.findByLoginEmail(loginEmail)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

        if (user == null) {
            return "fail";
        }
        else{
            return "success";
        }
    }
}
