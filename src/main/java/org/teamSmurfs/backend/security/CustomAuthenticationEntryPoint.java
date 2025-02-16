/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 08:40 PM
 */
package org.teamSmurfs.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.config.exception.UnauthorizedException;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("Unauthorized access attempt to {}", request.getRequestURI());
        throw new UnauthorizedException("You are not authorized to access this resource.");
    }
}