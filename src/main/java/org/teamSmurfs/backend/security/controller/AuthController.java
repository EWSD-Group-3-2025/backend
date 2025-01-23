/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:03 PM
 */
package org.teamSmurfs.backend.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.api.response.utils.ResponseUtil;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.base.path}/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        ApiResponse response = authService.authenticateUser(loginRequest);
        return ResponseUtil.buildResponse(request, response, loginRequest.getRequestTime());
    }
}
