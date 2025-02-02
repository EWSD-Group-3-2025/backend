/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:58 PM
 */
package org.teamSmurfs.backend.security.service;

import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RefreshTokenData;
import org.teamSmurfs.backend.security.dto.RegisterRequest;

public interface AuthService {
    ApiResponse authenticateUser(LoginRequest loginRequest);
    ApiResponse registerUser(RegisterRequest registerRequest);
    void logout(String accessToken, RefreshTokenData refreshTokenData);
    ApiResponse refreshToken(String refreshToken);
}
