/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:58 PM
 */
package org.teamSmurfs.backend.security.service;

import org.teamSmurfs.backend.api.response.dto.ApiResponse;
import org.teamSmurfs.backend.security.dto.LoginRequest;

public interface AuthService {
    ApiResponse authenticateUser(LoginRequest loginRequest);
}
