package com.example.passwordmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class PasswordManagerGUI extends Application {
    private List<Entry> entries = new ArrayList<>();
    private TextField nameField, usernameField, passwordField;
    private TextArea notesField;
    private ListView<String> entryList;
    private Label statusLabel;
    private String userId; 

    public PasswordManagerGUI(String userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Secure Password Manager");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        entryList = new ListView<>();
        entryList.setPrefHeight(150);
        grid.add(entryList, 0, 0, 2, 1);

        nameField = new TextField();
        usernameField = new TextField();
        passwordField = new TextField();
        notesField = new TextArea();

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Notes:"), 0, 4);
        grid.add(notesField, 1, 4);

        Button addButton = new Button("Add Entry");
        Button viewButton = new Button("View Entry");
        Button editButton = new Button("Edit Entry");
        Button deleteButton = new Button("Delete Entry");
        Button generateButton = new Button("Generate Password");
        statusLabel = new Label("");

        grid.add(addButton, 0, 5);
        grid.add(viewButton, 1, 5);
        grid.add(editButton, 0, 6);
        grid.add(deleteButton, 1, 6);
        grid.add(generateButton, 0, 7);
        grid.add(statusLabel, 0, 8, 2, 1);

        addButton.setOnAction(e -> addEntry());
        viewButton.setOnAction(e -> viewEntry());
        editButton.setOnAction(e -> editEntry());
        deleteButton.setOnAction(e -> deleteEntry());
        generateButton.setOnAction(e -> generatePassword());

        primaryStage.setScene(new Scene(grid, 420, 500));
        primaryStage.show();
    }

    private void addEntry() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String notes = notesField.getText();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠ Error: All fields must be filled.");
            return;
        }

        try {
            if (!PasswordValidator.isValid(password)) {
                statusLabel.setText("⚠ Weak password! Must have 8+ chars, uppercase, lowercase, digit, symbol.");
                return;
            }

            if (HIBPChecker.isCompromised(password)) {
                statusLabel.setText("❗ This password has been found in a data breach! Choose another.");
                return;
            }

            FirebaseService.savePasswordEntry(userId, name, username, password, notes);

            entries.add(new Entry(name, username, password, notes));
            entryList.getItems().add(name);
            clearFields();
            statusLabel.setText("✅ Entry saved online!");
        } catch (Exception ex) {
            statusLabel.setText("❌ Error: " + ex.getMessage());
        }
    }

    private void viewEntry() {
        String selected = entryList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select an entry to view.");
            return;
        }

        for (Entry entry : entries) {
            if (entry.getName().equals(selected)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("View Entry");
                alert.setHeaderText("Entry Details");
                alert.setContentText(
                        "Name: " + entry.getName() +
                        "\nUsername: " + entry.getUsername() +
                        "\nPassword: " + entry.getPassword() +
                        "\nNotes: " + entry.getNotes()
                );
                alert.showAndWait();
                return;
            }
        }
        statusLabel.setText("⚠ Entry not found.");
    }

    private void editEntry() {
        String selected = entryList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select an entry to edit.");
            return;
        }

        for (Entry entry : entries) {
            if (entry.getName().equals(selected)) {
                String newUsername = usernameField.getText();
                String newPassword = passwordField.getText();
                String newNotes = notesField.getText();

                if (!newUsername.isEmpty()) entry.setUsername(newUsername);

                if (!newPassword.isEmpty()) {
                    try {
                        if (!PasswordValidator.isValid(newPassword)) {
                            statusLabel.setText("⚠ Weak password!");
                            return;
                        }
                        if (HIBPChecker.isCompromised(newPassword)) {
                            statusLabel.setText("❗ Compromised password detected!");
                            return;
                        }
                        entry.setPassword(newPassword);
                    } catch (Exception e) {
                        statusLabel.setText("❌ Error checking password: " + e.getMessage());
                        return;
                    }
                }

                if (!newNotes.isEmpty()) entry.setNotes(newNotes);
                statusLabel.setText("✅ Entry updated!");
                return;
            }
        }
    }

    private void deleteEntry() {
        String selected = entryList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select an entry to delete.");
            return;
        }

        entries.removeIf(entry -> entry.getName().equals(selected));
        entryList.getItems().remove(selected);
        statusLabel.setText("🗑️ Entry deleted!");
    }

    private void generatePassword() {
        String password = PasswordGenerator.generate(16, true, true, true, true);
        passwordField.setText(password);
        statusLabel.setText("🔑 Strong password generated!");
    }

    private void clearFields() {
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        notesField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
