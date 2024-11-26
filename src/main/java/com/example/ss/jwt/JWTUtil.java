package com.example.ss.jwt;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// 0.12.3
@Component
public class JWTUtil {

    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private final String BEARER = "Bearer";

    private final String ACCESS_TOKEN_SUBJECT = "accessToken";

    private final String REFRESH_TOKEN_SUBJECT = "refreshToken";

    private final String USERNAME_CLAIM = "username";

    private final String ROLE_CLAIM = "role";


    JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(USERNAME_CLAIM, String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(ROLE_CLAIM, String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createAccessToken(String username, String role) {
        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .claim(USERNAME_CLAIM, username)
                .claim(ROLE_CLAIM, role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String username, String role) {
        return Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .claim(USERNAME_CLAIM, username)
                .claim(ROLE_CLAIM, role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    public void setAccessToken(HttpServletResponse response, String token) {
        response.addHeader(accessHeader, token);
    }

    public void setRefreshToken(HttpServletResponse response, String token) {
        response.addHeader(refreshHeader, token);
    }
}
