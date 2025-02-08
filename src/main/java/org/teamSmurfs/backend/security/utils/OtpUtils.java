/*
 * @Author : Thant Htoo Aung
 * @Date : 02/07/2025
 * @Time : 02:30 AM (UTC)
 */
package org.teamSmurfs.backend.security.utils;

import lombok.Value;

import java.time.Instant;
import java.util.Random;

public class OtpUtils {
    
    public static String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Value
    public static class OtpData {
        String email;
        Instant expiration;

        public boolean isExpired() {
            return Instant.now().isAfter(expiration);
        }
    }
}