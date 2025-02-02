/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:00 PM
 */
package org.teamSmurfs.backend.security.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.utils.DtoUtil;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RefreshTokenData;
import org.teamSmurfs.backend.security.dto.RegisterRequest;
import org.teamSmurfs.backend.security.service.AuthService;
import org.teamSmurfs.backend.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.security.utils.ClaimsProvider;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

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

        UserDto userDto = DtoUtil.map(user, UserDto.class, modelMapper);

        Map<String, Object> tokenData = generateTokens(user);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "user", userDto,
                        "accessToken", tokenData.get("accessToken"),
                        "refreshToken", tokenData.get("refreshToken")
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

        Map<String, Object> tokenData = generateTokens(newUser);

        log.info("User registered successfully: {}", registerRequest.getEmail());

        UserDto userDto = DtoUtil.map(newUser, UserDto.class, modelMapper);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(Map.of(
                        "user", userDto,
                        "accessToken", tokenData.get("accessToken"),
                        "refreshToken", tokenData.get("refreshToken")
                ))
                .message("You have registered successfully.")
                .build();
    }

    private Map<String, Object> generateTokens(User user) {
        log.debug("Generating tokens for user: {}", user.getEmail());

        String accessToken = jwtService.generateToken(ClaimsProvider.generateClaims(user), user.getEmail(), 15 * 60 * 1000);
        String refreshToken = jwtService.generateToken(ClaimsProvider.generateClaims(user), user.getEmail(), 7 * 24 * 60 * 60 * 1000);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    @Override
    public void logout(String accessToken, RefreshTokenData refreshTokenData) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            Claims claims = jwtService.validateToken(token);
            String userEmail = claims.getSubject();

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new SecurityException("User not found. Cannot proceed with logout."));

            log.debug("Revoking access token for user: {}", user.getEmail());
            jwtService.revokeToken(token);
        }

        if (refreshTokenData != null && refreshTokenData.getRefreshToken() != null) {
            String refreshToken = refreshTokenData.getRefreshToken().substring(7);
            Claims refreshClaims = jwtService.validateToken(refreshToken);
            String userEmail = refreshClaims.getSubject();

            if (!userRepository.existsByEmail(userEmail)) {
                throw new SecurityException("Invalid refresh token. User does not exist.");
            }

            log.debug("Revoking refresh token for user: {}", userEmail);
            jwtService.revokeToken(refreshToken);
        }

        log.info("User successfully logged out.");
    }

    @Override
    public ApiResponse refreshToken(String refreshToken) {
        log.info("Validating refresh token");

        Claims claims;
        try {
            claims = jwtService.validateToken(refreshToken);
        } catch (SecurityException ex) {
            log.warn("Invalid refresh token: {}", ex.getMessage());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .data(ex.getMessage())
                    .message("Invalid or expired refresh token")
                    .build();
        }

        String email = claims.getSubject();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.warn("User not found for refresh token: {}", email);
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .data(false)
                    .message("User not found")
                    .build();
        }

        log.info("Generating new access token for user: {}", email);

        String newAccessToken = jwtService.generateToken(ClaimsProvider.generateClaims(user), email, 15 * 60 * 1000);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("accessToken", newAccessToken))
                .message("Access token refreshed successfully")
                .build();
    }

    @Override
    public ApiResponse getCurrentUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header");
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized: Missing or invalid token")
                    .build();
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.validateToken(token);
        String email = claims.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new SecurityException("User not found");
                });

        UserDto userDto = DtoUtil.map(user, UserDto.class, modelMapper);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "user", userDto
                ))
                .message("User retrieved successfully")
                .build();
    }
}
