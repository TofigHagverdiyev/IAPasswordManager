package com.example.passwordmanager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseService {
    private static FirebaseAuth auth;
    private static Firestore db;

    public static void initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/google-services.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        auth = FirebaseAuth.getInstance();
        db = FirestoreClient.getFirestore();
    }

    public static String createUser(String email, String password) throws Exception {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setEmailVerified(false);

        UserRecord userRecord = auth.createUser(request);

        // ✅ Fix: Send verification email
        sendVerificationEmail(userRecord.getUid(), email);

        return userRecord.getUid();
    }

    public static void sendVerificationEmail(String userId, String email) throws Exception {
        // ✅ Fix: Use Firebase ActionCodeSettings to send email properly
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                .setUrl("https://tofig.page.link/verify-email?uid=" + userId)
                .setHandleCodeInApp(true)
                .build();

        String link = auth.generateEmailVerificationLink(email, actionCodeSettings);

        // ✅ Debug: Print the email verification link
        System.out.println("📧 Email verification link (for testing): " + link);

        // ✅ TODO: Send this link via email using an SMTP server or email provider.
    }

    public static String authenticateUser(String email, String password) {
        try {
            UserRecord userRecord = auth.getUserByEmail(email);
            
            if (!userRecord.isEmailVerified()) {
                System.out.println("⚠ Email not verified. Check your email.");
                return null;
            }

            return userRecord.getUid();
        } catch (Exception e) {
            return null;
        }
    }

    public static void savePasswordEntry(String userId, String name, String username, String password, String notes) {
        if (db == null) {
            System.out.println("⚠ Firestore database is not initialized.");
            return;
        }

        Map<String, Object> entry = new HashMap<>();
        entry.put("name", name);
        entry.put("username", username);
        entry.put("password", password);
        entry.put("notes", notes);

        db.collection("users").document(userId).collection("passwords").document(name).set(entry)
            .addListener(() -> System.out.println("✅ Password entry saved!"), Runnable::run);
    }
}
