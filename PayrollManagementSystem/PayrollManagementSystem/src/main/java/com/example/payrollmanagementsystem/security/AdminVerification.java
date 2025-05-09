package com.example.payrollmanagementsystem.security;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AdminVerification {
    private static final String ADMIN_TOKEN = "ADMIN123"; // In production, use a more secure method

    public static boolean verifyAdminRegistration() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin Verification");
        dialog.setHeaderText("Admin registration requires special privileges");
        dialog.setContentText("Enter admin registration token:");

        Optional<String> result = dialog.showAndWait();
        return result.isPresent() && ADMIN_TOKEN.equals(result.get());
    }
}