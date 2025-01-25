/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:00 PM
 */
package org.teamSmurfs.backend.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ApiResponse authenticateUser(LoginRequest loginRequest) {
        log.info("Authenticating user with email: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", loginRequest.getEmail());
                    return new SecurityException("Invalid email or password");
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", loginRequest.getEmail());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid email or password")
                    .build();
        }

        log.info("User authenticated successfully: {}", loginRequest.getEmail());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", "USER");

        String accessToken = jwtService.generateToken(claims, user.getEmail(), 15 * 60 * 1000);
        String refreshToken = jwtService.generateToken(claims, user.getEmail(), 7 * 24 * 60 * 60 * 1000);

        log.debug("Generated access and refresh tokens for user: {}", user.getEmail());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                ))
                .message("You are successfully logged in!")
                .build();
    }

    @Override
    public ApiResponse registerUser(RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Email already exists: {}", registerRequest.getEmail());
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

        log.info("User registered successfully: {}", registerRequest.getEmail());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("You have registered successfully.")
                .build();
    }
}
