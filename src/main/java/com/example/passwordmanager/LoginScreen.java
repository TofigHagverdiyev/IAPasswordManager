package com.example.passwordmanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreen extends Application {
    private TextField emailField;
    private PasswordField passwordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FirebaseService.initializeFirebase(); // Initialize Firebase

        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Email:"), 0, 0);
        emailField = new TextField();
        grid.add(emailField, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        statusLabel = new Label("");

        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);
        grid.add(statusLabel, 0, 3, 2, 1);

        loginButton.setOnAction(e -> login(primaryStage));
        registerButton.setOnAction(e -> register(primaryStage));

        primaryStage.setScene(new Scene(grid, 300, 200));
        primaryStage.show();
    }

    private void login(Stage primaryStage) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠ Email and Password required.");
            return;
        }

        try {
            String userId = FirebaseService.authenticateUser(email, password);
            if (userId == null) {
                statusLabel.setText("⚠ Login failed: Email not verified or incorrect credentials.");
                return;
            }

            primaryStage.close();
            new PasswordManagerGUI(userId).start(new Stage()); // FIXED: Now passes userId
        } catch (Exception ex) {
            statusLabel.setText("❌ Login failed: " + ex.getMessage());
        }
    }

    private void register(Stage primaryStage) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠ Email and Password required.");
            return;
        }

        try {
            String userId = FirebaseService.createUser(email, password);
            statusLabel.setText("✅ Account created! Check your email for a verification link.");
        } catch (Exception e) {
            statusLabel.setText("❌ Registration failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
