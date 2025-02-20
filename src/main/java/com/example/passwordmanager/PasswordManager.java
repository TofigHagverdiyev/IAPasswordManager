package com.example.passwordmanager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordManager {
    private static final Scanner scan = new Scanner(System.in);
    private static final List<Entry> entries = new ArrayList<>();

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.println("Welcome to the Secure Password Manager!");
        while (true) {
            System.out.println("\n1. Add Entry\n2. View Entries\n3. Edit Entry\n4. Delete Entry\n5. Exit");
            String choice = scan.nextLine();
            // switch cases
            switch (choice) {
                case "1": addEntry(); break;
                case "2": viewEntries(); break;
                case "3": editEntry(); break;
                case "4": deleteEntry(); break;
                case "5": System.exit(0);
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addEntry() {
        System.out.println("Enter Name:");
        String name = scan.nextLine();
        System.out.println("Enter Username:");
        String username = scan.nextLine();
        System.out.println("Enter Password:");
        String password = scan.nextLine();
        System.out.println("Enter Notes (optional):");
        String notes = scan.nextLine();
        entries.add(new Entry(name, username, password, notes));
        System.out.println("Entry added successfully!");
    }

    private static void viewEntries() {
        if (entries.isEmpty()) {
            System.out.println("No entries found.");
        } else {
            for (Entry entry : entries) {
                System.out.println(entry);
            }
        }
    }

    private static void editEntry() {
        System.out.println("Enter entry name to edit:");
        String name = scan.nextLine();
        for (Entry entry : entries) {
            if (entry.getName().equalsIgnoreCase(name)) {
                System.out.println("Enter new username:");
                entry.setUsername(scan.nextLine());
                System.out.println("Enter new password:");
                entry.setPassword(scan.nextLine());
                System.out.println("Enter new notes:");
                entry.setNotes(scan.nextLine());
                System.out.println("Entry updated!");
                return;
            }
        }
        System.out.println("Entry not found.");
    }

    private static void deleteEntry() {
        System.out.println("Enter entry name to delete:");
        String name = scan.nextLine();
        entries.removeIf(entry -> entry.getName().equalsIgnoreCase(name));
        System.out.println("Entry deleted.");
    }
}
