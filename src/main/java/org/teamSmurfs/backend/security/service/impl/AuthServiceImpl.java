/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:00 PM
 */
package org.teamSmurfs.backend.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RegisterRequest;
import org.teamSmurfs.backend.security.service.AuthService;
import org.teamSmurfs.backend.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ApiResponse authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new SecurityException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid email or password")
                    .build();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", "USER");

        String accessToken = jwtService.generateToken(claims, user.getEmail(), 15 * 60 * 1000);
        String refreshToken = jwtService.generateToken(claims, user.getEmail(), 7 * 24 * 60 * 60 * 1000);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                ))
                .message("User authenticated successfully")
                .build();
    }

    @Override
    public ApiResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Email is already in use")
                    .build();
        }

        User newUser = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(newUser);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .build();
    }
}
