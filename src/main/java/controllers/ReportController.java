package controllers;

import utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class ReportController {

    @FXML
    private PieChart payrollExpenseChart;

    public void generateReport() {
        String query = "SELECT SUM(net_salary) AS total_salary FROM payroll";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                double totalSalary = rs.getDouble("total_salary");

                // Add data to chart
                PieChart.Data slice = new PieChart.Data("Total Salary", totalSalary);
                payrollExpenseChart.getData().add(slice);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Report generated!");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error generating report.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Error generating report.");
            alert.show();
        }
    }
}
