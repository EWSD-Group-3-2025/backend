/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:03 PM
 */
package org.teamSmurfs.backend.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.api.request.RequestUtils;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RegisterRequest;
import org.teamSmurfs.backend.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.teamSmurfs.backend.security.service.JwtService;

@RestController
@RequestMapping("/${api.base.path}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    public final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("Received login attempt for email: {}", loginRequest.getEmail());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ApiResponse response = authService.authenticateUser(loginRequest);

        if (response.getSuccess() == 1) {
            log.info("Login successful for user: {}", loginRequest.getEmail());
        } else {
            log.warn("Login failed for user: {}", loginRequest.getEmail());
        }

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

        @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        log.info("Received logout request with token: {}", token);

        if (token.startsWith("Bearer ")) {
            log.debug("Revoking token for logout process: {}", token.substring(7));
            jwtService.revokeToken(token.substring(7));
        }
        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Logout successful")
                .build();

        log.info("User logged out successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        log.info("Received registration request for email: {}", registerRequest.getEmail());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ApiResponse response = authService.registerUser(registerRequest);

        if (response.getSuccess() == 1) {
            log.info("User registered successfully: {}", registerRequest.getEmail());
        } else {
            log.warn("Registration failed for email: {}", registerRequest.getEmail());
        }

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
