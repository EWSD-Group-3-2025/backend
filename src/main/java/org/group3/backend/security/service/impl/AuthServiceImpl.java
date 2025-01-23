/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:00 PM
 */
package org.group3.backend.security.service.impl;

import org.group3.backend.api.response.dto.ApiResponse;
import org.group3.backend.security.dto.LoginRequest;
import org.group3.backend.security.service.AuthService;
import org.group3.backend.security.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public ApiResponse authenticateUser(LoginRequest loginRequest) {
        boolean isAuthenticated = "admin@kmd.edu.mm".equals(loginRequest.getEmail())
                && "password".equals(loginRequest.getPassword());
        if (isAuthenticated) {
            Map<String, Object> userData = getUserData();
            String accessToken = JwtUtil.generateToken(userData, "admin", 15 * 60 * 1000); // 15 minutes
            String refreshToken = JwtUtil.generateToken(userData, "admin", 7 * 24 * 60 * 60 * 1000); // 7 days

            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .data(Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken
                    ))
                    .message("User Login Successfully")
                    .build();
        } else {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid email or password")
                    .duration((double) (Instant.now().getEpochSecond() - loginRequest.getRequestTime()))
                    .build();
        }
    }

    private static Map<String, Object> getUserData() {
        Long userId = 1L;
        String userName = "Admin";
        String userEmail = "admin@kmd.edu.mm";
        Long roleId = 1L;
        Long genderId = 1L;
        String password = "password";
        String address = "123 Main Street";
        boolean enabled = true;

        return Map.of(
                "id", userId,
                "name", userName,
                "email", userEmail,
                "roleId", roleId,
                "genderId", genderId,
                "phone", password,
                "address", address,
                "enabled", enabled
        );
    }
}
