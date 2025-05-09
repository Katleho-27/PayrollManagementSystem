package com.example.payrollmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML private StackPane contentPane;

    private void loadUI(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/com/example/payrollmanagementsystem/" + fxml + ".fxml"));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            showError("Failed to load " + fxml, e.getMessage());
        }
    }

    @FXML
    public void handleEmployee() {
        loadUI("Employee");
    }

    @FXML
    public void handleSalary() {
        loadUI("Salary");
    }

    @FXML
    public void handlePayslip() {
        loadUI("Payslip");
    }

    @FXML
    public void handleReports() {
        loadUI("Report");
    }

    @FXML
    public void handleLogout() {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/com/example/payrollmanagementsystem/Login.fxml"));
            contentPane.getScene().setRoot((Parent) node);
        } catch (IOException e) {
            showError("Failed to logout", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}