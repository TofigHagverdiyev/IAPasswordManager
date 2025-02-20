package com.example.passwordmanager;

public class Entry {
    private String name;
    private String username;
    private String password;
    private String notes;

    public Entry(String name, String username, String password, String notes) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNotes() { return notes; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Name: " + name + "\nUsername: " + username + "\nPassword: " + password + "\nNotes: " + notes;
    }
}
