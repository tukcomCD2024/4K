package springwebsocket.webchat.member.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springwebsocket.webchat.global.jwt.TokenProvider;
import springwebsocket.webchat.global.response.ApiResponse;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.TokenMessage;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.member.repository.MemberRepository;
import springwebsocket.webchat.member.repository.RefreshMemberRepository;

import java.util.Optional;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshMemberRepository refreshMemberRepository;


    public MemberServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider, RefreshMemberRepository refreshMemberRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshMemberRepository = refreshMemberRepository;
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

    public TokenMessage login(String loginEmail, String password) {

        Optional<Member> user = memberRepository.findByLoginEmail(loginEmail)
                .filter(m -> bCryptPasswordEncoder.matches(password, m.getPassword()));

        if (!user.isEmpty()) {
            return handleExistingMemberLogin(user.get());
        } else {
            throw null;
        }
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refreshToken = request.getHeader("Refresh-Token");

        if (refreshToken == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            tokenProvider.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = tokenProvider.getCategory(refreshToken);

        if (!category.equals("refreshToken")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshMemberRepository.existsByRefresh(refreshToken);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = tokenProvider.getEmail(refreshToken);

        //make new JWT
        String newAccessToken = tokenProvider.createAccessToken(email);
        String newRefreshToken = tokenProvider.createRefreshToken(email);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshMemberRepository.deleteByRefresh(refreshToken);

        return ResponseEntity.ok()
                .header("Access-Token", newAccessToken)
                .header("Refresh-Token", newRefreshToken)
                .body("재발급 성공");
    }

    public ResponseEntity<?> logout() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //get refresh token
        String refreshToken = request.getHeader("Refresh-Token");

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshMemberRepository.deleteByRefresh(refreshToken);

        return ResponseEntity.ok().body("로그아웃 성공");
    }

    private TokenMessage handleExistingMemberLogin(Member user) {
        String accessToken = tokenProvider.createAccessToken(user.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        return new TokenMessage(accessToken, refreshToken);
    }
}
