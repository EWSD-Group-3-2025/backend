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
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.config.deprecated.DeprecatedRoute;
import org.teamSmurfs.backend.config.exception.UnauthorizedException;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RefreshTokenData;
import org.teamSmurfs.backend.security.dto.RegisterRequest;
import org.teamSmurfs.backend.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.teamSmurfs.backend.security.service.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/${api.base.path}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    public final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            @RequestParam(required = false) final String routeName,
            @RequestParam(required = false) final String browserName,
            @RequestParam(required = false) final String pageName
    ) {
        log.info("Received login attempt for email: {}", loginRequest.getEmail());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ApiResponse response = authService.authenticateUser(loginRequest, routeName, browserName, pageName);

        if (response.getSuccess() == 1) {
            log.info("Login successful for user: {}", loginRequest.getEmail());
        } else {
            log.warn("Login failed for user: {}", loginRequest.getEmail());
        }

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            HttpServletRequest request) {
        log.info("Received logout request");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        if ((accessToken == null || !accessToken.startsWith("Bearer "))) {

            log.warn("Invalid or missing tokens in logout request");
            throw new UnauthorizedException("Invalid or missing authorization tokens.");
        }

        try {
            authService.logout(accessToken);
            ApiResponse response = ApiResponse.builder()
                    .success(1)
                    .code(200)
                    .data(true)
                    .message("Logout successful")
                    .build();

            log.info("User logged out successfully");

            return ResponseUtil.buildResponse(request, response, requestStartTime);
        } catch (UnauthorizedException ex) {
            log.warn("Logout failed due to security reasons: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during logout", ex);
            throw new RuntimeException("An error occurred during logout.");
        }
    }

    @PostMapping("/register")
//    @DeprecatedRoute(message = "This endpoint is deprecated. Use /new-endpoint instead.")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request) {
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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@Validated @RequestBody RefreshTokenData refreshTokenData,
            HttpServletRequest request) {
        log.info("Received token refresh request");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        ApiResponse response = authService.refreshToken(refreshTokenData.getRefreshToken());

        if (response.getSuccess() == 1) {
            log.info("Token refreshed successfully");
        } else {
            log.warn("Token refresh failed");
        }

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(
            @RequestHeader("Authorization") final String authHeader,
            @RequestParam(required = false) final String routeName,
            @RequestParam(required = false) final String browserName,
            @RequestParam(required = false) final String pageName,
            HttpServletRequest request) {
        log.info("Fetching current authenticated user");

        double requestStartTime = System.currentTimeMillis();
        ApiResponse response = authService.getCurrentUser(authHeader, routeName, browserName, pageName);

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        log.info("Received forgot password request");
        double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);

        ApiResponse response = authService.initiatePasswordReset(request.get("email"));
        return ResponseUtil.buildResponse(httpRequest, response, requestStartTime);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        log.info("Received OTP verification request");
        double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);

        ApiResponse response = authService.verifyOtp(request.get("otp"));
        return ResponseUtil.buildResponse(httpRequest, response, requestStartTime);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        log.info("Received password reset request");
        double requestStartTime = RequestUtils.extractRequestStartTime(httpRequest);

        ApiResponse response = authService.resetPassword(
                request.get("newPassword"),
                request.get("confirmPassword"));
        return ResponseUtil.buildResponse(httpRequest, response, requestStartTime);
    }

    @PatchMapping("/updateMe")
    public ResponseEntity<ApiResponse> updateUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserDto userDto,
            HttpServletRequest request) {

        log.info("Updating user information");

        double requestStartTime = System.currentTimeMillis();
        ApiResponse response = authService.updateUser(authHeader, userDto);

        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

}
