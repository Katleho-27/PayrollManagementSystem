package controllers;

import models.User;
import utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void login(KeyEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                // Redirect to different dashboards based on role
                if ("admin".equals(role)) {
                    // Navigate to admin dashboard
                } else if ("employee".equals(role)) {
                    // Navigate to employee dashboard
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Invalid credentials.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Error logging in.");
            alert.show();
        }
    }
}
