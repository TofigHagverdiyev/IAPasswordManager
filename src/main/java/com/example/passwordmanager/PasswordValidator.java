package com.example.passwordmanager;

public class PasswordValidator {
    public static boolean isValid(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&  // at least one uppercase letter
               password.matches(".*[a-z].*") &&  // at least one lowercase letter
               password.matches(".*\\d.*") &&    // at least one digit
               password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/].*"); // at least one special character
    }
}
