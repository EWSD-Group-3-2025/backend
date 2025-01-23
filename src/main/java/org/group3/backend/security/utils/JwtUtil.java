/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 10:26 PM
 */
package org.group3.backend.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

//    @Value("${jwt.secret.key}")
//    private String secretKey;

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("bh5pYpZP4QuAlHFF4NljKIQD9QWU8HmX2wyKAiBaArk=".getBytes());

    public static String generateToken(Map<String, Object> claims, String role, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}