package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeDashboardController {
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void handleViewPayslip(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/payrollmanagementsystem/Payslip.fxml"));
            Parent root = loader.load();
            PayslipController controller = loader.getController();
            controller.setEmployeeId(currentUser.getEmployeeId());

            Stage stage = new Stage();
            stage.setTitle("My Payslip");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();

            closeCurrentWindow(event);
        } catch (IOException e) {
            showError("Failed to load payslip", e.getMessage());
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText("Are you sure you want to logout?");
        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                loadScene("/com/example/payrollmanagementsystem/Login.fxml", "Login");
                closeCurrentWindow(event);
            }
        });
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load " + title, e.getMessage());
        }
    }

    private void closeCurrentWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}