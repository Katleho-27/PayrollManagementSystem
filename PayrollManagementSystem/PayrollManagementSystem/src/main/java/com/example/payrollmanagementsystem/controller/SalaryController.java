package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.database.DatabaseConnection;
import com.example.payrollmanagementsystem.database.DatabaseOperations;
import com.example.payrollmanagementsystem.model.Employee;
import com.example.payrollmanagementsystem.model.Salary;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalaryController {
    @FXML private Button employeeButton;
    @FXML private Button salaryButton;
    @FXML private Button payslipButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    @FXML private ComboBox<Employee> employeeDropdown;
    @FXML private TextField basicSalaryField, overtimeHoursField, allowancesField, taxField, deductionsField, netSalaryField;
    @FXML private TableView<Salary> salaryTable;
    @FXML private TableColumn<Salary, String> colEmpId, colName, colBasic, colOvertime, colAllowances, colTax, colDeductions, colNetSalary;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private ObservableList<Salary> salaryList = FXCollections.observableArrayList();
    private static final double TAX_RATE = 0.2; // 20% tax rate
    private static final double OVERTIME_RATE = 1.5; // 1.5x regular hourly rate

    @FXML
    public void initialize() {
        // Load employees
        loadEmployeeDropdown();
        employeeDropdown.setItems(employeeList);

        // Set up table columns
        colEmpId.setCellValueFactory(cellData -> cellData.getValue().employeeIdProperty());
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getName()));
        colBasic.setCellValueFactory(cellData -> cellData.getValue().basicSalaryProperty().asString("%.2f"));
        colOvertime.setCellValueFactory(cellData -> cellData.getValue().overtimePayProperty().asString("%.2f"));
        colAllowances.setCellValueFactory(cellData -> cellData.getValue().allowancesProperty().asString("%.2f"));
        colTax.setCellValueFactory(cellData -> cellData.getValue().taxProperty().asString("%.2f"));
        colDeductions.setCellValueFactory(cellData -> cellData.getValue().deductionsProperty().asString("%.2f"));
        colNetSalary.setCellValueFactory(cellData -> cellData.getValue().netSalaryProperty().asString("%.2f"));

        // Load salary data
        salaryTable.setItems(salaryList);
        loadSalaryData();

        // Bind employee selection
        employeeDropdown.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                basicSalaryField.setText(String.format("%.2f", newValue.getBasicSalary()));
                overtimeHoursField.setText("0");
                allowancesField.setText("0.0");
                deductionsField.setText("0.0");
                calculateNetSalary();
            }
        });
    }

    @FXML
    public void handleEmployee() {
        loadScreen("/com/example/payrollmanagementsystem/Employee.fxml", "Employee Management");
    }

    @FXML
    public void handleSalary() {
        // Already on Salary screen
    }

    @FXML
    public void handlePayslip() {
        loadScreen("/com/example/payrollmanagementsystem/Payslip.fxml", "Payslip Generator");
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
            ((Stage) salaryTable.getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Error loading screen: " + e.getMessage());
        }
    }

    private void loadEmployeeDropdown() {
        employeeList.clear();
        employeeList.addAll(DatabaseOperations.getAllEmployees());
    }

    private void loadSalaryData() {
        salaryList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT s.*, e.name FROM salaries s JOIN employees e ON s.employee_id = e.id";
            PreparedStatement stmt = conn.prepareStatement(query);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getString("employee_id"),
                        rs.getString("name"),
                        "", "",
                        rs.getDouble("basic_salary"),
                        0
                );
                Salary salary = new Salary(
                        emp,
                        rs.getDouble("basic_salary"),
                        rs.getDouble("overtime_pay"),
                        rs.getDouble("allowances"),
                        rs.getDouble("tax"),
                        rs.getDouble("deductions"),
                        rs.getDouble("net_salary")
                );
                salaryList.add(salary);
            }
        } catch (SQLException e) {
            showError("Failed to load salary data: " + e.getMessage());
        }
    }

    @FXML
    public void handleCalculate() {
        calculateNetSalary();
    }

    private void calculateNetSalary() {
        try {
            Employee selectedEmployee = employeeDropdown.getSelectionModel().getSelectedItem();
            if (selectedEmployee == null) {
                showError("Please select an employee.");
                return;
            }

            double basic = Double.parseDouble(basicSalaryField.getText());
            double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
            double allowances = Double.parseDouble(allowancesField.getText());
            double deductions = Double.parseDouble(deductionsField.getText());

            double hourlyRate = basic / (selectedEmployee.getWorkingHours() * 4);
            double overtimePay = overtimeHours * hourlyRate * OVERTIME_RATE;

            double grossSalary = basic + overtimePay + allowances;
            double tax = grossSalary * TAX_RATE;

            double net = grossSalary - tax - deductions;
            netSalaryField.setText(String.format("%.2f", net));
            taxField.setText(String.format("%.2f", tax));
        } catch (NumberFormatException e) {
            showError("Please enter valid numerical values for salary fields.");
        }
    }

    @FXML
    public void handleSave() {
        Employee selectedEmployee = employeeDropdown.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showError("Please select an employee.");
            return;
        }

        try {
            double basic = Double.parseDouble(basicSalaryField.getText());
            double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
            double allowances = Double.parseDouble(allowancesField.getText());
            double deductions = Double.parseDouble(deductionsField.getText());
            double tax = Double.parseDouble(taxField.getText());
            double netSalary = Double.parseDouble(netSalaryField.getText());

            double hourlyRate = basic / (selectedEmployee.getWorkingHours() * 4);
            double overtimePay = overtimeHours * hourlyRate * OVERTIME_RATE;

            Salary salary = new Salary(
                    selectedEmployee,
                    basic,
                    overtimePay,
                    allowances,
                    tax,
                    deductions,
                    netSalary
            );

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO salaries (employee_id, basic_salary, overtime_pay, allowances, tax, deductions, net_salary, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selectedEmployee.getId());
                stmt.setDouble(2, basic);
                stmt.setDouble(3, overtimePay);
                stmt.setDouble(4, allowances);
                stmt.setDouble(5, tax);
                stmt.setDouble(6, deductions);
                stmt.setDouble(7, netSalary);
                stmt.executeUpdate();

                salaryList.add(salary);
                showInfo("Salary record saved successfully!");
                clearFields();
            } catch (SQLException e) {
                showError("Failed to save salary: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid numerical values for salary fields.");
        }
    }

    @FXML
    public void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Salary selectedSalary = salaryTable.getSelectionModel().getSelectedItem();
            if (selectedSalary != null) {
                employeeDropdown.getSelectionModel().select(selectedSalary.getEmployee());
                basicSalaryField.setText(String.format("%.2f", selectedSalary.getBasicSalary()));
                overtimeHoursField.setText(String.format("%.2f", selectedSalary.getOvertimePay() /
                        (selectedSalary.getBasicSalary() / (selectedSalary.getEmployee().getWorkingHours() * 4) * OVERTIME_RATE)));
                allowancesField.setText(String.format("%.2f", selectedSalary.getAllowances()));
                taxField.setText(String.format("%.2f", selectedSalary.getTax()));
                deductionsField.setText(String.format("%.2f", selectedSalary.getDeductions()));
                netSalaryField.setText(String.format("%.2f", selectedSalary.getNetSalary()));
            }
        }
    }

    private void clearFields() {
        employeeDropdown.getSelectionModel().clearSelection();
        basicSalaryField.clear();
        overtimeHoursField.clear();
        allowancesField.clear();
        taxField.clear();
        deductionsField.clear();
        netSalaryField.clear();
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