package springwebsocket.webchat.global.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import springwebsocket.webchat.global.jwt.TokenProvider;
import springwebsocket.webchat.global.jwt.dto.CustomUserDetails;
import springwebsocket.webchat.member.entity.Member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JWTFilter doFilterInternal");

        //request에서 access 헤더 찾기
        String accessToken = request.getHeader("access");

        //토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            tokenProvider.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = tokenProvider.getCategory(accessToken);

        if (!category.equals("accessToken")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // email, role값 획득
        String email = tokenProvider.getEmail(accessToken);
        String role = tokenProvider.getRole(accessToken);

        log.info("email = {}", email);
        log.info("role = {}", role);

        //memberEntity를 생성하여 값 set
        Member member = new Member();
        member.setEmail(email);
        member.setRole(role);

        //userDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(Optional.of(member));

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }
}
