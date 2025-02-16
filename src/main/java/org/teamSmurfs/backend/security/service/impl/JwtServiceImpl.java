/*
 * @Author : Thant Htoo Aung
 * @Date : 1/24/2025
 * @Time : 08:41 AM
 */
package org.teamSmurfs.backend.security.service.impl;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.config.exception.UnauthorizedException;
import org.teamSmurfs.backend.security.service.JwtService;
import org.teamSmurfs.backend.security.utils.JwtUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtServiceImpl implements JwtService {

    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public Claims validateToken(String token) {
        if (!JwtUtil.isTokenValid(token)) {
            throw new UnauthorizedException("Invalid or expired token.");
        }

        if (isTokenRevoked(token)) {
            throw new UnauthorizedException("Token has been revoked.");
        }

        return JwtUtil.decodeToken(token);
    }

    @Override
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    private boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }

    @Override
    public String generateToken(Map<String, Object> claims, String roleName, String subject, long expirationMillis) {
        return JwtUtil.generateToken(claims, roleName, subject, expirationMillis);
    }
}