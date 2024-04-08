package springwebsocket.webchat.global.jwt.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springwebsocket.webchat.global.jwt.JWTUtil;
import springwebsocket.webchat.global.jwt.TokenProvider;
import springwebsocket.webchat.global.jwt.dto.CustomUserDetails;
import springwebsocket.webchat.member.entity.RefreshMember;
import springwebsocket.webchat.member.repository.RefreshMemberRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter{


    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final RefreshMemberRepository refreshMemberRepository;

    public LoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, RefreshMemberRepository refreshMemberRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshMemberRepository = refreshMemberRepository;
    }

    // AuthenticationManager 여기서 인증
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication");
        //클라이언트 요청에서 username, password 추출
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        log.info("email = {}", email);
        log.info("password = {}", password);

        /**
         * UsernamePasswordAuthenticationFilter에서 AuthenticationManager로 전달 할 떼,
         * Dto를 통해서 전달을 하게 된다.
         * 이떄, UsernamePasswordAuthenticationToken(username, password, null)이게 Dto역할을 하게 됨.
         */
        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("successfulAuthentication");
        //UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();     //getPrincipal은 사용자의 정보를 반환

        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = tokenProvider.createAccessToken(email);
//        String refresh = tokenProvider.createAccessToken(email);

        //Refresh Token 저장
//        addRefreshEntity(email, refresh, 86400000L);


        /**
         * 예시 : Http 인증 방식은 아래 인증 헤더 형태를 가져야 한다.
         * Authorization: Bearer 인증토큰 string
         * Bearer : 인증방식
         */
        //응답 설정
        response.setHeader("access", access);
//        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("unsuccessfulAuthentication");
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //        cookie.setSecure(true); //https 를 적용 할 경우 true 값 설정;
        //        cookie.setPath("/");  //쿠키가 적용될 부분
        cookie.setHttpOnly(true);   //클라이언트단에서 자바 스크립트로 접근할 수 없도록 필수적으로 설정해야 할 부분

        return cookie;
    }

    // RefreshToken 저장 함수
    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshMember refreshMember = new RefreshMember();
        refreshMember.setEmail(email);
        refreshMember.setRefresh(refresh);
        refreshMember.setExpiration(date.toString());

        refreshMemberRepository.save(refreshMember);
    }
}