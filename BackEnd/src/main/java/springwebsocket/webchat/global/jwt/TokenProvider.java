package springwebsocket.webchat.global.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import springwebsocket.webchat.global.jwt.service.CustomUserDetailsService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final CustomUserDetailsService customUserDetailsService;
    private final SecretKey secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;

    public TokenProvider(CustomUserDetailsService customUserDetailsService, @Value("${spring.jwt.secret}") String secret) {
        this.customUserDetailsService = customUserDetailsService;
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpirationTime = 3 * 60 * 60 * 1000L;       // 3 hours
        this.refreshExpirationTime = 15 * 24 * 60 * 60 * 1000L;  // 15 days
    }


    @Override
    public void afterPropertiesSet() throws Exception {
//        // jwt 원시 키를 디코딩하고 jwt 서명하는데 사용
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성 메서드
//    public String createAccessToken(String uid) {
//
//        Claims claims = Jwts.claims().setSubject(uid);
//        claims.put("token_type", "accessToken");
//
//        Date expirationTime = getExpirationTime(accessExpirationTime);
//
//        String accessToken = Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(expirationTime)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return accessToken;
//
//    }

    /**
     * 로그인이 성공했을 때, LoginFilter의 successfulAuthentication 메소드 호출을 통해
     * username, role, expiredMs를 전달받아
     * 토큰을 생성할 메소드
     */
    public String createAccessToken(String email) {
        Claims claims = Jwts.claims().setSubject(email).build();

        // claim은 payload에 추가
        return Jwts.builder()
                .claim("category", "accessToken")    // category = {access, refresh}
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(secretKey)
                .compact();
    }

    // 토큰에서 정보 추출
//    public Authentication getAuthentication(String token) {
//
//        String uid = parseClaims(token).getSubject();
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(uid);
//
//        return new UsernamePasswordAuthenticationToken(userDetails, token);
//    }
//
//    // Access Token 검증
//    public boolean validateAccessToken(String accessToken) {
//        try {
//            Claims claims = parseClaims(accessToken);
//
//            // 토큰 타입 확인
//            String tokenType = (String) claims.get("token_type");
//            if (!"accessToken".equals(tokenType)) {
//                return false;
//            }
//            // 만료 날짜 확인
//            return !claims.getExpiration().before(new Date());
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.warn("잘못된 JWT 서명입니다.", e);
//        } catch (ExpiredJwtException e) {
//            log.warn("만료된 JWT입니다.", e);
//        } catch (UnsupportedJwtException e) {
//            log.warn("지원되지 않는 JWT입니다.", e);
//        } catch (IllegalArgumentException e) {
//            log.warn("잘못된 JWT입니다.", e);
//        }
//
//        return false;
//    }
//
//    // Refresh Token 검증
//    public boolean validateRefreshToken(String refreshToken) {
//        try {
//            Claims claims = parseClaims(refreshToken);
//
//            // 토큰 타입 확인
//            String tokenType = (String) claims.get("token_type");
//            if (!"refreshToken".equals(tokenType)) {
//                return false;
//            }
//
//            // 만료 날짜 확인
//            return !claims.getExpiration().before(new Date());
//
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.warn("잘못된 JWT 서명입니다.", e);
//        } catch (ExpiredJwtException e) {
//            log.warn("만료된 JWT입니다.", e);
//        } catch (UnsupportedJwtException e) {
//            log.warn("지원되지 않는 JWT입니다.", e);
//        } catch (IllegalArgumentException e) {
//            log.warn("잘못된 JWT입니다.", e);
//        }
//
//        return false;
//    }


//    public String getUid(String token) {
//        return parseClaims(token).getSubject();
//    }

    // JWT 키 해독
//    private Claims parseClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

    private Date getExpirationTime(Long expirationTime) {
        return new Date((new Date()).getTime() + expirationTime);
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
