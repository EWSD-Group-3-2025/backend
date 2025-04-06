/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:58 PM
 */
package org.teamSmurfs.backend.security.service;

import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.user.dto.UserDto;
import org.teamSmurfs.backend.security.dto.LoginRequest;
import org.teamSmurfs.backend.security.dto.RegisterRequest;

public interface AuthService {
    ApiResponse authenticateUser(LoginRequest loginRequest, String routeName, String browserName, String pageName);

    ApiResponse registerUser(RegisterRequest registerRequest);

    void logout(String accessToken);

    ApiResponse refreshToken(String refreshToken);

    ApiResponse getCurrentUser(final String authHeader, final String routeName, final String browserName, final String pageName);

    ApiResponse initiatePasswordReset(String email);

    ApiResponse verifyOtp(String otp);

    ApiResponse resetPassword(String newPassword, String confirmPassword);

    ApiResponse updateUser(String authHeader, UserDto userDto);
}
