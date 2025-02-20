package com.example.passwordmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class HIBPChecker {
    private static final String API_URL = "https://api.pwnedpasswords.com/range/";

    public static boolean isCompromised(String password) throws Exception {
        String hash = sha1(password).toUpperCase();
        String prefix = hash.substring(0, 5);
        String suffix = hash.substring(5);

        URL url = new URL(API_URL + prefix);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "PasswordManagerApp/1.0"); // REQUIRED!

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(":")[0].equals(suffix)) {
                    return true; // password is compromised
                }
            }
        } catch (Exception e) {
            System.out.println("Error connecting to HIBP API: " + e.getMessage());
        }

        return false; // password is safe
    }

    private static String sha1(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = md.digest(input.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) hex.append(String.format("%02x", b));
        return hex.toString();
    }
}
