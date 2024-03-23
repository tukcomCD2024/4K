package springwebsocket.webchat.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import springwebsocket.webchat.global.jwt.JWTUtil;
import springwebsocket.webchat.global.jwt.dto.CustomUserDetails;
import springwebsocket.webchat.member.entity.Member;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JWTFilter doFilterInternal");

        //request에서 Authorization 헤더 찾기
        String authorization = request.getHeader("Authorization");

        //Authorization헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        log.info("authorization get");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            log.info("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        //token에서 username과 role획득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        //memberEntity를 생성하여 값 set
        Member member = new Member();
        member.setEmail(email);
        member.setRole(role);
        member.setPassword("asdfasfd");

        //userDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(Optional.of(member));

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }
}
