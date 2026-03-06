package space.changle.lynnia.security.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import space.changle.lynnia.common.enums.LoginType;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.result.ApiResult;
import space.changle.lynnia.common.util.JsonUtils;
import space.changle.lynnia.security.filter.JwtAuthenticationFilter;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/1 23:36
 * @description 安全配置
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /// 过滤器链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/tmalogin", "/api/auth/weblogin","/api/auth/register").permitAll()
                        .requestMatchers("/api/tma/**").hasAuthority(LoginType.TMA.authority())
                        .requestMatchers("/api/web/**").hasAuthority(LoginType.WEB.authority())
                        .anyRequest().authenticated()
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /// 认证失败
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Result result = (Result) request.getAttribute("TOKEN_ERROR");
            if (result == null) {result = Result.TOKEN_INVALID;}
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.toJson(ApiResult.error(result)));
        };
    }

    /// 拒绝访问
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.toJson(ApiResult.error(Result.ACCESS_DENIED)));
        };
    }
}
