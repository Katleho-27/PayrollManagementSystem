package controllers;

import models.Payslip;
import utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;

public class PayslipController {

    @FXML
    private TextField employeeIdField;

    @FXML
    private TextArea payslipDetailsArea;

    public void generatePayslip() {
        int employeeId = Integer.parseInt(employeeIdField.getText());

        String query = "SELECT * FROM payroll WHERE employee_id = ? ORDER BY pay_date DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double grossSalary = rs.getDouble("gross_salary");
                double deductions = rs.getDouble("deductions");
                double netSalary = rs.getDouble("net_salary");
                Date payDate = rs.getDate("pay_date");

                String payslipDetails = "Employee ID: " + employeeId +
                        "\nGross Salary: " + grossSalary +
                        "\nDeductions: " + deductions +
                        "\nNet Salary: " + netSalary +
                        "\nPay Date: " + payDate;

                payslipDetailsArea.setText(payslipDetails);

                // Save payslip to database
                String payslipQuery = "INSERT INTO payslips (employee_id, pay_date, payslip_details) VALUES (?, ?, ?)";
                try (PreparedStatement payslipPs = conn.prepareStatement(payslipQuery)) {
                    payslipPs.setInt(1, employeeId);
                    payslipPs.setDate(2, payDate);
                    payslipPs.setString(3, payslipDetails);
                    payslipPs.executeUpdate();
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Payslip generated!");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("No payroll record found for this employee.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Error generating payslip.");
            alert.show();
        }
    }
}
