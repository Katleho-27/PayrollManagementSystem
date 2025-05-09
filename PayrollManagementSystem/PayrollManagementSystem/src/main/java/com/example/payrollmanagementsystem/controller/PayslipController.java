package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.database.DatabaseConnection;
import com.example.payrollmanagementsystem.model.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayslipController {
    @FXML private Button employeeButton;
    @FXML private Button salaryButton;
    @FXML private Button payslipButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    @FXML private VBox payslipContainer;
    @FXML private TextField txtEmpId, txtName, txtDepartment, txtPosition, txtBasic, txtOvertime, txtAllowance, txtTax, txtDeduction, txtNetSalary;
    @FXML private Label lblStatus;
    private String employeeId;

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
        loadPayslipData();
    }

    @FXML
    public void initialize() {
        // Initialization if needed
    }

    @FXML
    public void handleEmployee() {
        loadScreen("/com/example/payrollmanagementsystem/Employee.fxml", "Employee Management");
    }

    @FXML
    public void handleSalary() {
        loadScreen("/com/example/payrollmanagementsystem/Salary.fxml", "Salary Calculation");
    }

    @FXML
    public void handlePayslip() {
        // Already on Payslip screen
    }

    @FXML
    public void handleReports() {
        loadScreen("/com/example/payrollmanagementsystem/Report.fxml", "Reports & Charts");
    }

    @FXML
    public void handleLogout() {
        loadScreen("/com/example/payrollmanagementsystem/Login.fxml", "Login - Payroll Management System");
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
            // Close current window
            ((Stage) payslipContainer.getScene().getWindow()).close();
        } catch (IOException e) {
            lblStatus.setText("Error loading screen: " + e.getMessage());
        }
    }

    private void loadPayslipData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT e.id, e.name, e.department, e.position, s.basic_salary, s.overtime_pay, s.allowances, s.tax, s.deductions, s.net_salary " +
                    "FROM employees e LEFT JOIN salaries s ON e.id = s.employee_id WHERE e.id = ? ORDER BY s.id DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtEmpId.setText(rs.getString("id"));
                txtName.setText(rs.getString("name"));
                txtDepartment.setText(rs.getString("department"));
                txtPosition.setText(rs.getString("position"));
                txtBasic.setText(String.format("%.2f", rs.getDouble("basic_salary")));
                txtOvertime.setText(String.format("%.2f", rs.getDouble("overtime_pay")));
                txtAllowance.setText(String.format("%.2f", rs.getDouble("allowances")));
                txtTax.setText(String.format("%.2f", rs.getDouble("tax")));
                txtDeduction.setText(String.format("%.2f", rs.getDouble("deductions")));
                txtNetSalary.setText(String.format("%.2f", rs.getDouble("net_salary")));
            } else {
                lblStatus.setText("No payslip data found for this employee.");
            }
        } catch (SQLException e) {
            lblStatus.setText("Error loading payslip data: " + e.getMessage());
        }
    }

    @FXML
    public void handlePrint() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(payslipContainer.getScene().getWindow())) {
            boolean success = job.printPage(payslipContainer);
            if (success) {
                job.endJob();
                showInfo("Payslip printed successfully!");
            } else {
                showError("Failed to print payslip.");
            }
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) payslipContainer.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}