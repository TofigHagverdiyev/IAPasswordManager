package com.example.passwordmanager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private String plainText;
    private String hashedText;

    public Hash(String plainText) {
        this.plainText = plainText;
    }

    public void hashString() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(this.plainText.getBytes(StandardCharsets.UTF_8));
        this.hashedText = bytesToHex(hashBytes);
    }

    private String bytesToHex(byte[] hashBytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            hexString.append(String.format("%02x", hashByte));
        }
        return hexString.toString();
    }

    public String getHashedText() { return hashedText; }
}
