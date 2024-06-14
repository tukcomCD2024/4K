package springwebsocket.webchat.global.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springwebsocket.webchat.member.entity.RefreshMember;
import springwebsocket.webchat.member.repository.RefreshMemberRepository;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    private final SecretKey secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;
    private final RefreshMemberRepository refreshMemberRepository;

    public TokenProvider(@Value("${spring.jwt.secret}") String secret, RefreshMemberRepository refreshMemberRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshMemberRepository = refreshMemberRepository;
        this.accessExpirationTime = 3 * 60 * 60 * 1000L;       // 3 hours
        this.refreshExpirationTime = 15 * 24 * 60 * 60 * 1000L;  // 15 days
    }

    /**
     * 로그인이 성공했을 때, LoginFilter의 successfulAuthentication 메소드 호출을 통해
     * username, role, expiredMs를 전달받아
     * 토큰을 생성할 메소드
     */
    public String createAccessToken(String email) {
        // claim은 payload에 추가
        return Jwts.builder()
                .claim("category", "accessToken")    // category = {access, refresh}
                .claim("email", email)
                .claim("role","ROLE_ADMIN")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(secretKey)
                .compact();
    }


    public String createRefreshToken(String email) {

        // claim은 payload에 추가
        String refreshToken = Jwts.builder()
                .claim("category", "refreshToken")    // category = {access, refresh}
                .claim("email", email)
                .claim("role", "ROLE_ADMIN")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(secretKey)
                .compact();

        //Refresh Token 저장
        addRefreshEntity(email, refreshToken, refreshExpirationTime);

        return refreshToken;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshMember refreshMember = new RefreshMember();
        refreshMember.setEmail(email);
        refreshMember.setRefresh(refresh);
        refreshMember.setExpiration(date.toString());

        refreshMemberRepository.save(refreshMember);
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

}
