package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.database.DatabaseOperations;
import com.example.payrollmanagementsystem.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField passwordField;
    @FXML private Label lblStatus;

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required.");
            return;
        }

        try {
            User user = DatabaseOperations.authenticateUser(username, password);
            if (user != null) {
                showInfo("Login successful!");
                loadDashboard(user);
            } else {
                showError("Invalid credentials.");
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGoToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/payrollmanagementsystem/SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Register - Payroll Management System");
            stage.setResizable(false);
            stage.show();

            ((Stage) txtUsername.getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Error loading sign-up screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDashboard(User user) {
        try {
            String fxmlPath = user.getRole().equals("admin") ?
                    "/com/example/payrollmanagementsystem/AdminDashboard.fxml" :
                    "/com/example/payrollmanagementsystem/EmployeeDashboard.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (user.getRole().equals("employee")) {
                EmployeeDashboardController controller = loader.getController();
                controller.setCurrentUser(user);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1) + " Dashboard");
            stage.setResizable(true);
            stage.show();

            ((Stage) txtUsername.getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setStyle("-fx-text-fill: #e74c3c;");
    }

    private void showInfo(String message) {
        lblStatus.setText(message);
        lblStatus.setStyle("-fx-text-fill: #2ecc71;");
    }
}