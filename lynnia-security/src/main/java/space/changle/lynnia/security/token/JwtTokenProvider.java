package space.changle.lynnia.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import space.changle.lynnia.common.enums.LoginType;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.TokenException;
import space.changle.lynnia.security.config.JwtProperties;
import space.changle.lynnia.security.enums.TokenType;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/1 00:45
 * @description 令牌提供者
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final SecretKey accessKey;

    private final SecretKey refreshKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.accessKey = buildKey(jwtProperties.getAccess().getSecret());
        this.refreshKey = buildKey(jwtProperties.getRefresh().getSecret());
    }

    public String generateAccessToken(String userId, LoginType loginType) {
        return generateToken(userId, loginType, TokenType.ACCESS, accessKey);
    }

    public String generateRefreshToken(String userId, LoginType loginType) {
        return generateToken(userId, loginType, TokenType.REFRESH, refreshKey);
    }

    public Claims parseAccessToken(String token) {
        Claims claims = parse(token, accessKey);
        if (claims != null && !TokenType.ACCESS.name().equals(claims.get("token_type"))) {
            throw new TokenException(Result.TOKEN_INVALID);
        }

        return claims;
    }

    public Claims parseRefreshToken(String token) {
        Claims claims = parse(token, refreshKey);
        if (claims != null && !TokenType.REFRESH.name().equals(claims.get("token_type"))) {
            throw new TokenException(Result.TOKEN_INVALID);
        }
        return claims;
    }

    public String getUserId(Claims claims) {
        return claims.getSubject();
    }

    private String generateToken(String userId, LoginType loginType, TokenType tokenType, SecretKey key) {
        JwtProperties.Client client = jwtProperties.getClient(loginType);
        if (!client.canIssue(tokenType)) {
            throw new IllegalStateException("禁止使用 token");
        }

        JwtProperties.Token config = tokenType == TokenType.ACCESS ? jwtProperties.getAccess() : jwtProperties.getRefresh();
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(config.getExpireSeconds());

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(userId)
                .claim("client_type", loginType.name())
                .claim("token_type", tokenType.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private Claims parse(String token, SecretKey key) {
        try {

            return Jwts.parser()
                    .requireIssuer(jwtProperties.getIssuer())
                    .verifyWith( key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenException(Result.TOKEN_EXPIRED);
        }catch (JwtException e) {
            throw new TokenException(Result.TOKEN_INVALID);
        }
    }

    private SecretKey buildKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
