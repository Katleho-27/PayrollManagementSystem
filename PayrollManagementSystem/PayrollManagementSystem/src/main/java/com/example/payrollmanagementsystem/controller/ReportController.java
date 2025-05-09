package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportController {
    @FXML private Button employeeButton;
    @FXML private Button salaryButton;
    @FXML private Button payslipButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    @FXML private PieChart departmentPieChart;
    @FXML private BarChart<String, Number> salaryBarChart;
    @FXML private LineChart<String, Number> trendLineChart;

    @FXML
    public void initialize() {
        setupDepartmentChart();
        setupSalaryChart();
        setupTrendChart();
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
        loadScreen("/com/example/payrollmanagementsystem/Payslip.fxml", "Payslip Generator");
    }

    @FXML
    public void handleReports() {
        // Already on Reports screen
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
            ((Stage) departmentPieChart.getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Error loading screen: " + e.getMessage());
        }
    }

    private void setupDepartmentChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT department, COUNT(*) as count FROM employees GROUP BY department";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pieData.add(new PieChart.Data(rs.getString("department"), rs.getInt("count")));
            }
        } catch (SQLException e) {
            showError("Failed to load department chart: " + e.getMessage());
        }
        departmentPieChart.setData(pieData);
        departmentPieChart.setTitle("Employee Distribution by Department");
    }

    private void setupSalaryChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Salary");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT department, AVG(basic_salary) as avg_salary FROM employees GROUP BY department";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("department"), rs.getDouble("avg_salary")));
            }
        } catch (SQLException e) {
            showError("Failed to load salary chart: " + e.getMessage());
        }
        salaryBarChart.getData().add(series);
        salaryBarChart.setTitle("Average Salary by Department");
    }

    private void setupTrendChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Payroll");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, SUM(net_salary) as total FROM salaries GROUP BY month ORDER BY month";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("month"), rs.getDouble("total")));
            }
        } catch (SQLException e) {
            showError("Failed to load trend chart: " + e.getMessage());
        }
        trendLineChart.getData().add(series);
        trendLineChart.setTitle("Payroll Trend Over Time");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}