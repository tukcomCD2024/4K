package springwebsocket.webchat.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.dto.UserUpdateDto;
import springwebsocket.webchat.repository.MemberRepository;

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
    public void update(Long userId, UserUpdateDto updateParam) {
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
}
