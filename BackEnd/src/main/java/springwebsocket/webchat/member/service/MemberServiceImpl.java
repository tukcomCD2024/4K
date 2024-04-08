package springwebsocket.webchat.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.global.jwt.TokenProvider;
import springwebsocket.webchat.global.jwt.filter.LoginFilter;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.member.repository.MemberRepository;

import java.util.Optional;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;


    public MemberServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public UserResponse signUp(SignUpRequest signUpRequest) {
        String name = signUpRequest.getName();
        String email = signUpRequest.getEmail();
        String password = bCryptPasswordEncoder.encode(signUpRequest.getPassword());

        Member member = new Member(email, password, name, "ROLE_ADMIN");
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException ex) {
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

    public ResponseEntity<String> login(String loginEmail, String password) {
        log.info("/member/login/service");
        log.info("loginEmail = {}", loginEmail);
        log.info("password ={}", password);
        Optional<Member> user = memberRepository.findByLoginEmail(loginEmail)
                .filter(m -> bCryptPasswordEncoder.matches(password, m.getPassword()));

        if (!user.isEmpty()) {
            log.info("user!=null");
            return handleExistingMemberLogin(user.get());
        }
        else{
            return ResponseEntity.ok().body("로그인 실패");
        }
    }

    private ResponseEntity<String> handleExistingMemberLogin(Member user) {

        String accessToken = tokenProvider.createAccessToken(user.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());
        return ResponseEntity.ok()
                .header("Access-Token", accessToken)
                .header("Refresh-Token", refreshToken)
                .body("로그인 성공");
    }
}
