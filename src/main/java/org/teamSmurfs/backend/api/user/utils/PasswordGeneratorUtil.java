package org.teamSmurfs.backend.api.user.utils;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGeneratorUtil {

    private static final int DEFAULT_PASSWORD_LENGTH = 12;
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqstrstuvwxyz0123456789!@#$%^&*()_+";

    private static final Random RANDOM = new SecureRandom();

    private PasswordGeneratorUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    public static String generatePassword() {
        return generatePassword(DEFAULT_PASSWORD_LENGTH);
    }

    public static String generatePassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters.");
        }

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(ALLOWED_CHARACTERS.charAt(RANDOM.nextInt(ALLOWED_CHARACTERS.length())));
        }

        return password.toString();
    }
}
