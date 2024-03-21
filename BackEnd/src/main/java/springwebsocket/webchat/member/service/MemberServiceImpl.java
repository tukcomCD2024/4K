package springwebsocket.webchat.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.member.repository.MemberRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;



    @Override
    @Transactional
    public UserResponse signUp(SignUpRequest signUpRequest) {
        String name = signUpRequest.getName();
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        Member member = new Member(email, password, name);
        try {
            memberRepository.save(member);
        }catch (DataIntegrityViolationException ex){
            throw new EmailDuplicatedException(email);

        }
        return new UserResponse(member.getName(), member.getEmail());
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

    public String login(String loginEmail, String password){
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
