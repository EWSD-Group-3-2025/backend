/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:03 PM
 */
package org.teamSmurfs.backend.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class AuthController {

    private final AuthService authService;
    public final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = authService.authenticateUser(loginRequest);
        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            jwtService.revokeToken(token.substring(7));
        }
        ApiResponse response = ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Logout successful")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        double requestStartTime = RequestUtils.extractRequestStartTime(request);
        ApiResponse response = authService.registerUser(registerRequest);
        return ResponseUtil.buildResponse(request, response, requestStartTime);
    }
}
