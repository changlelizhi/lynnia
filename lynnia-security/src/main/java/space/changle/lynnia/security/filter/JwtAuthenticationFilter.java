package space.changle.lynnia.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import space.changle.lynnia.common.enums.LoginType;
import space.changle.lynnia.common.exception.TokenException;
import space.changle.lynnia.security.token.JwtTokenProvider;

import java.io.IOException;
import java.util.List;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/1 00:45
 * @description 认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        try {
            if (token != null) {
                Claims claims = jwtTokenProvider.parseAccessToken(token);
                String userId = jwtTokenProvider.getUserId(claims);
                LoginType loginType = LoginType.valueOf(claims.get("client_type", String.class));

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(loginType.authority()));
                Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (TokenException e){
            SecurityContextHolder.clearContext();
            request.setAttribute("TOKEN_ERROR", e.getResult());
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        log.info("bearer: {}", bearer);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
