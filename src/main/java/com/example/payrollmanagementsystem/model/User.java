package com.example.payrollmanagementsystem.model;

import javafx.beans.property.*;

public class User {
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty role;
    private final StringProperty employeeId;

    public User(String username, String password, String role, String employeeId) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.employeeId = new SimpleStringProperty(employeeId); // Initialize with provided employeeId or null
    }

    // Property getters
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty roleProperty() { return role; }
    public StringProperty employeeIdProperty() { return employeeId; }

    // Standard getters
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getRole() { return role.get(); }
    public String getEmployeeId() { return employeeId.get(); }

    // Setters
    public void setUsername(String username) { this.username.set(username); }
    public void setPassword(String password) { this.password.set(password); }
    public void setRole(String role) { this.role.set(role); }
    public void setEmployeeId(String employeeId) { this.employeeId.set(employeeId); }
}