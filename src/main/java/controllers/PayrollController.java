package controllers;

import models.Payroll;
import utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import java.sql.*;

public class PayrollController {

    @FXML
    private TextField employeeIdField;

    @FXML
    private TextField grossSalaryField;

    @FXML
    private TextField deductionsField;

    @FXML
    private TextField netSalaryField;

    public void calculatePayroll() {
        int employeeId = Integer.parseInt(employeeIdField.getText());

        String query = "SELECT * FROM employees WHERE employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double basicSalary = rs.getDouble("basic_salary");
                int workingHours = rs.getInt("working_hours");
                double overtimeRate = rs.getDouble("overtime_rate");

                double overtimePay = (workingHours > 40) ? (workingHours - 40) * overtimeRate : 0;
                double grossSalary = basicSalary + overtimePay;

                // Assuming a flat 10% tax and 5% insurance
                double tax = grossSalary * 0.10;
                double insurance = grossSalary * 0.05;
                double deductions = tax + insurance;
                double netSalary = grossSalary - deductions;

                grossSalaryField.setText(String.valueOf(grossSalary));
                deductionsField.setText(String.valueOf(deductions));
                netSalaryField.setText(String.valueOf(netSalary));

                // Save payroll to database
                String payrollQuery = "INSERT INTO payroll (employee_id, gross_salary, deductions, net_salary, pay_date) VALUES (?, ?, ?, ?, CURDATE())";
                try (PreparedStatement payrollPs = conn.prepareStatement(payrollQuery)) {
                    payrollPs.setInt(1, employeeId);
                    payrollPs.setDouble(2, grossSalary);
                    payrollPs.setDouble(3, deductions);
                    payrollPs.setDouble(4, netSalary);
                    payrollPs.executeUpdate();
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Payroll calculated and saved!");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Employee not found.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Error calculating payroll.");
            alert.show();
        }
    }
}
