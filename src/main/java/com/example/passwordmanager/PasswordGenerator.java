package com.example.passwordmanager;

import java.util.Random;

public class PasswordGenerator {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";

    public static String generate(int length, boolean useLower, boolean useUpper, boolean useDigits, boolean useSymbols) {
        if (length < 8) length = 8; // Ensure minimum security

        StringBuilder charPool = new StringBuilder();
        if (useLower) charPool.append(LOWER);
        if (useUpper) charPool.append(UPPER);
        if (useDigits) charPool.append(DIGITS);
        if (useSymbols) charPool.append(SYMBOLS);

        if (charPool.length() == 0) return "Error: No character set selected.";

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(charPool.charAt(random.nextInt(charPool.length())));
        }
        return password.toString();
    }
}
