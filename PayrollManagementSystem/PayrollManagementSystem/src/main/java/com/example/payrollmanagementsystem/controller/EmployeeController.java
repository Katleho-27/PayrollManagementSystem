package com.example.payrollmanagementsystem.controller;

import com.example.payrollmanagementsystem.database.DatabaseConnection;
import com.example.payrollmanagementsystem.database.DatabaseOperations;
import com.example.payrollmanagementsystem.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeController {
    @FXML private Button employeeButton;
    @FXML private Button salaryButton;
    @FXML private Button payslipButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    @FXML private TextField idField, nameField, departmentField, positionField, salaryField, hoursField;
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> colId, colName, colDepartment, colPosition;
    @FXML private TableColumn<Employee, Double> colSalary;
    @FXML private TableColumn<Employee, Integer> colHours;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colDepartment.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        colPosition.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        colSalary.setCellValueFactory(cellData -> cellData.getValue().basicSalaryProperty().asObject());
        colHours.setCellValueFactory(cellData -> cellData.getValue().workingHoursProperty().asObject());

        // Load employees
        employeeList.setAll(DatabaseOperations.getAllEmployees());
        employeeTable.setItems(employeeList);

        // Populate fields on row selection
        employeeTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newValue) -> {
                    if (newValue != null) {
                        idField.setText(newValue.getId());
                        nameField.setText(newValue.getName());
                        departmentField.setText(newValue.getDepartment());
                        positionField.setText(newValue.getPosition());
                        salaryField.setText(String.format("%.2f", newValue.getBasicSalary()));
                        hoursField.setText(String.valueOf(newValue.getWorkingHours()));
                    }
                });
    }

    @FXML
    public void handleEmployee() {
        // Already on Employee screen
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
            ((Stage) employeeTable.getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Error loading screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO employees (id, name, department, position, basic_salary, working_hours) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idField.getText());
            stmt.setString(2, nameField.getText());
            stmt.setString(3, departmentField.getText());
            stmt.setString(4, positionField.getText());
            stmt.setDouble(5, Double.parseDouble(salaryField.getText()));
            stmt.setInt(6, Integer.parseInt(hoursField.getText()));
            stmt.executeUpdate();

            Employee emp = new Employee(
                    idField.getText(),
                    nameField.getText(),
                    departmentField.getText(),
                    positionField.getText(),
                    Double.parseDouble(salaryField.getText()),
                    Integer.parseInt(hoursField.getText())
            );
            employeeList.add(emp);
            clearFields();
            showInfo("Employee added successfully!");
        } catch (SQLException | NumberFormatException e) {
            showError("Failed to add employee: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an employee to update.");
            return;
        }
        if (!validateInput()) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE employees SET name = ?, department = ?, position = ?, basic_salary = ?, working_hours = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, departmentField.getText());
            stmt.setString(3, positionField.getText());
            stmt.setDouble(4, Double.parseDouble(salaryField.getText()));
            stmt.setInt(5, Integer.parseInt(hoursField.getText()));
            stmt.setString(6, idField.getText());
            stmt.executeUpdate();

            selected.setName(nameField.getText());
            selected.setDepartment(departmentField.getText());
            selected.setPosition(positionField.getText());
            selected.setBasicSalary(Double.parseDouble(salaryField.getText()));
            selected.setWorkingHours(Integer.parseInt(hoursField.getText()));
            employeeTable.refresh();
            clearFields();
            showInfo("Employee updated successfully!");
        } catch (SQLException | NumberFormatException e) {
            showError("Failed to update employee: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an employee to delete.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM employees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, selected.getId());
            stmt.executeUpdate();

            employeeList.remove(selected);
            clearFields();
            showInfo("Employee deleted successfully!");
        } catch (SQLException e) {
            showError("Failed to delete employee: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        if (idField.getText().trim().isEmpty()) errors.append("Employee ID is required.\n");
        if (nameField.getText().trim().isEmpty()) errors.append("Name is required.\n");
        if (departmentField.getText().trim().isEmpty()) errors.append("Department is required.\n");
        if (positionField.getText().trim().isEmpty()) errors.append("Position is required.\n");
        if (salaryField.getText().trim().isEmpty()) errors.append("Salary is required.\n");
        if (hoursField.getText().trim().isEmpty()) errors.append("Working hours are required.\n");

        try {
            Double.parseDouble(salaryField.getText());
            Integer.parseInt(hoursField.getText());
        } catch (NumberFormatException e) {
            errors.append("Salary must be a number and hours must be an integer.\n");
        }

        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }
        return true;
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        departmentField.clear();
        positionField.clear();
        salaryField.clear();
        hoursField.clear();
        employeeTable.getSelectionModel().clearSelection();
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