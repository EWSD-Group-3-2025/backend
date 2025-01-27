/*
 * @Author : Thant Htoo Aung
 * @Date : 1/24/2025
 * @Time : 08:41 AM
 */
package org.teamSmurfs.backend.security.service;

import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {
    Claims validateToken(String token);

    void revokeToken(String token);

    String generateToken(Map<String, Object> claims, String subject, long expirationMillis);
}