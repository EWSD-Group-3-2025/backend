/*
 * @Author : Thant Htoo Aung
 * @Date : 1/2/2025
 * @Time : 02:49 PM (Thailand time)
 */
package org.teamSmurfs.backend.security.utils;

import org.teamSmurfs.backend.features.user.model.User;

import java.util.HashMap;
import java.util.Map;

public class ClaimsProvider {

    private ClaimsProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", "USER");
        return claims;
    }
}
