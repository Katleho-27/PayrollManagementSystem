package controllers;

import models.Employee;
import utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;

public class EmployeeController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField basicSalaryField;
    @FXML
    private TextField workingHoursField;
    @FXML
    private TextField overtimeRateField;

    // Add Employee
    public void addEmployee() {
        String name = nameField.getText();
        String department = departmentField.getText();
        String position = positionField.getText();
        double basicSalary = Double.parseDouble(basicSalaryField.getText());
        int workingHours = Integer.parseInt(workingHoursField.getText());
        double overtimeRate = Double.parseDouble(overtimeRateField.getText());

        String query = "INSERT INTO employees (name, department, position, basic_salary, working_hours, overtime_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setString(3, position);
            ps.setDouble(4, basicSalary);
            ps.setInt(5, workingHours);
            ps.setDouble(6, overtimeRate);

            ps.executeUpdate();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Employee added successfully!");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Error adding employee.");
            alert.show();
        }
    }
}