package com.example.payrollmanagementsystem.database;

import com.example.payrollmanagementsystem.model.Employee;
import com.example.payrollmanagementsystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    // Option 1: Allow any Employee ID without validation
    public static String registerUser(String username, String password, String role, String employeeId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                return "Username already exists.";
            }

            // Validate input for employee role
            if (role.equalsIgnoreCase("employee")) {
                if (employeeId == null || employeeId.trim().isEmpty()) {
                    return "Employee ID is required for employee role.";
                }
                // No validation against employees table (Option 1)
            } else {
                employeeId = null; // Ensure employeeId is null for admin
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (username, password, role, employee_id) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password); // TODO: Hash password in production
            insertStmt.setString(3, role);
            insertStmt.setString(4, employeeId);
            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0 ? "Success" : "Failed to insert user.";
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }

    /*
    // Option 2: Auto-create employee record
    public static String registerUser(String username, String password, String role, String employeeId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                return "Username already exists.";
            }

            // Validate input for employee role
            if (role.equalsIgnoreCase("employee")) {
                if (employeeId == null || employeeId.trim().isEmpty()) {
                    return "Employee ID is required for employee role.";
                }
                // Check if employeeId already exists in employees
                String empCheckQuery = "SELECT COUNT(*) FROM employees WHERE id = ?";
                PreparedStatement empCheckStmt = conn.prepareStatement(empCheckQuery);
                empCheckStmt.setString(1, employeeId);
                ResultSet empRs = empCheckStmt.executeQuery();
                empRs.next();
                if (empRs.getInt(1) > 0) {
                    return "Employee ID " + employeeId + " is already in use.";
                }

                // Auto-create employee record with default values
                String insertEmpQuery = "INSERT INTO employees (id, name, department, position, basic_salary, working_hours) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertEmpStmt = conn.prepareStatement(insertEmpQuery);
                insertEmpStmt.setString(1, employeeId);
                insertEmpStmt.setString(2, "Pending Name"); // Placeholder
                insertEmpStmt.setString(3, "Pending Department");
                insertEmpStmt.setString(4, "Pending Position");
                insertEmpStmt.setDouble(5, 0.0); // Default salary
                insertEmpStmt.setInt(6, 40); // Default hours
                insertEmpStmt.executeUpdate();
            } else {
                employeeId = null; // Ensure employeeId is null for admin
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (username, password, role, employee_id) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password); // TODO: Hash password in production
            insertStmt.setString(3, role);
            insertStmt.setString(4, employeeId);
            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0 ? "Success" : "Failed to insert user.";
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    */

    public static User authenticateUser(String username, String password) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // TODO: Compare hashed passwords
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("employee_id")
                );
            }
            return null;
        }
    }

    public static ObservableList<Employee> getAllEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM employees";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("position"),
                        rs.getDouble("basic_salary"),
                        rs.getInt("working_hours")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}