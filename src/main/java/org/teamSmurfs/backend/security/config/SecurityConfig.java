/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 08:38 PM
 */
package org.teamSmurfs.backend.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.core.Authentication;
import org.teamSmurfs.backend.security.CustomAuthenticationEntryPoint;
import org.teamSmurfs.backend.security.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
    private static final String ROLE_STAFF = ROLE_PREFIX + "STAFF";
    private static final String ROLE_USER = ROLE_PREFIX + "USER";
    private static final String ROLE_TUTOR = ROLE_PREFIX + "TUTOR";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(this::configureAuthorization)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }

    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/public/**", "/api/v1/users/change-password").permitAll()

                .requestMatchers("/api/v1/users/**", "/api/v1/mail/**").access(hasRole(ROLE_USER))
                .requestMatchers("/api/v1/tutor/**").access(hasRole(ROLE_TUTOR))
                .requestMatchers("/api/v1/staff/**").access(hasRole(ROLE_STAFF))
                .requestMatchers("/api/v1/admin/**").access(hasRole(ROLE_ADMIN))

                .anyRequest().authenticated();
    }

    private AuthorizationManager<RequestAuthorizationContext> hasRole(String requiredRole) {
        return (Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) -> {
            HttpServletRequest request = context.getRequest();
            String userRole = getRoleFromToken(request);
            return new AuthorizationDecision(userRole.equals(requiredRole));
        };
    }

    private String getRoleFromToken(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .orElse(null);

        if (token == null || token.isEmpty()) {
            return "";
        }

        return Optional.ofNullable(JwtUtil.decodeToken(token))
                .map(claims -> claims.get("role", String.class))
                .orElse("");
    }
}