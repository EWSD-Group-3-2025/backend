/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:58 PM
 */
package org.group3.backend.security.service;

import org.group3.backend.api.response.dto.ApiResponse;
import org.group3.backend.security.dto.LoginRequest;

public interface AuthService {
    ApiResponse authenticateUser(LoginRequest loginRequest);
}
