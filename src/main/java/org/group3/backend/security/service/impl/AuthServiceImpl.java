/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2024
 * @Time : 10:00 PM
 */
package org.group3.backend.security.service.impl;

import org.group3.backend.api.response.dto.ApiResponse;
import org.group3.backend.security.dto.LoginRequest;
import org.group3.backend.security.service.AuthService;
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
            String token = UUID.randomUUID().toString();
            Map<String, Object> userData = getUserData();

            return ApiResponse.builder()
                    .success(1)
                    .code(HttpStatus.OK.value())
                    .meta(Map.of(
                            "method", "POST",
                            "endpoint", "api/auth/login",
                            "limit", 0,
                            "offset", 0,
                            "total", 1
                    ))
                    .data(Map.of(
                            "userData", userData,
                            "role", "admin",
                            "token", token
                    ))
                    .message("User Login Successfully")
                    .duration((double) (Instant.now().getEpochSecond() - loginRequest.getRequestTime()))
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
                "role_id", roleId,
                "gender_id", genderId,
                "phone", password,
                "address", address,
                "enabled", enabled
        );
    }
}
