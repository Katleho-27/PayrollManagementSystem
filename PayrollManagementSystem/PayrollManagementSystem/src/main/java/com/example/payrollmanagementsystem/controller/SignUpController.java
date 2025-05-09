package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.database.DatabaseOperations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private TextField employeeIdField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll("Employee", "Admin");
        roleChoiceBox.setValue("Employee");
        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            employeeIdField.setVisible(newValue.equals("Employee"));
            employeeIdField.setManaged(newValue.equals("Employee"));
        });
        employeeIdField.setVisible(true);
        employeeIdField.setManaged(true);
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleChoiceBox.getValue().toLowerCase();
        String employeeId = role.equals("employee") ? employeeIdField.getText().trim() : null;

        if (!validateRegistrationInput(username, password, role, employeeId)) {
            return;
        }

        try {
            String result = DatabaseOperations.registerUser(username, password, role, employeeId);
            if (result.equals("Success")) {
                showInfo("Registration successful! Please log in.");
                clearFields();
                handleGoToLogin();
            } else {
                showError(result);
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    public void handleGoToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/payrollmanagementsystem/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Payroll Management System");
            stage.setResizable(false);
            stage.show();

            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Error loading login screen: " + e.getMessage());
        }
    }

    private boolean validateRegistrationInput(String username, String password, String role, String employeeId) {
        StringBuilder errors = new StringBuilder();
        if (username.isEmpty()) {
            errors.append("Username is required.\n");
        }
        if (password.isEmpty()) {
            errors.append("Password is required.\n");
        }
        if (role.equals("employee") && (employeeId == null || employeeId.isEmpty())) {
            errors.append("Employee ID is required for employee role.\n");
        }
        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }
        return true;
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        employeeIdField.clear();
        roleChoiceBox.setValue("Employee");
        statusLabel.setText("");
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    private void showInfo(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #2ecc71;");
    }
}