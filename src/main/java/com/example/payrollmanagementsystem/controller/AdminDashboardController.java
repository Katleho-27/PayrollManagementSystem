package com.example.payrollmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {
    @FXML private Button employeeButton;
    @FXML private Button salaryButton;
    @FXML private Button payslipButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Welcome to the Admin Dashboard.");
        statusLabel.setStyle("-fx-text-fill: #2ecc71;");
    }

    @FXML
    public void handleEmployee() {
        loadScreen("/com/example/payrollmanagementsystem/employee.fxml", "Employee Management");
    }

    @FXML
    public void handleSalary() {
        loadScreen("/com/example/payrollmanagementsystem/salary.fxml", "Salary Management");
    }

    @FXML
    public void handlePayslip() {
        loadScreen("/com/example/payrollmanagementsystem/payslip.fxml", "Payslip Management");
    }

    @FXML
    public void handleReports() {
        loadScreen("/com/example/payrollmanagementsystem/Report.fxml", "Reports");
    }

    @FXML
    public void handleLogout() {
        loadScreen("/com/example/payrollmanagementsystem/Login.fxml", "Login - Payroll Management System");
        ((Stage) logoutButton.getScene().getWindow()).close();
    }

    private void loadScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("Error loading screen: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }
}