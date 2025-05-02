package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {

    @FXML
    private Label totalEmployeesLabel;
    @FXML
    private Label totalPayrollLabel;
    @FXML
    private PieChart salaryDistributionChart;
    @FXML
    private BarChart<String, Number> payrollBarChart;
    @FXML
    private LineChart<String, Number> payrollTrendChart;

    private Connection conn;

    public void initialize() {
        conn = DBConnection.getConnection();
        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            // Load total employees
            String employeeCountQuery = "SELECT COUNT(*) AS total FROM employees";
            PreparedStatement empStmt = conn.prepareStatement(employeeCountQuery);
            ResultSet empRs = empStmt.executeQuery();
            if (empRs.next()) {
                totalEmployeesLabel.setText(String.valueOf(empRs.getInt("total")));
            }

            // Load total payroll expenses
            String totalPayrollQuery = "SELECT SUM(net_salary) AS total FROM payroll";
            PreparedStatement payrollStmt = conn.prepareStatement(totalPayrollQuery);
            ResultSet payrollRs = payrollStmt.executeQuery();
            if (payrollRs.next()) {
                totalPayrollLabel.setText("M " + payrollRs.getDouble("total"));
            }

            // Populate salary distribution pie chart
            String pieQuery = "SELECT department, SUM(net_salary) AS total FROM payroll JOIN employees USING (employee_id) GROUP BY department";
            PreparedStatement pieStmt = conn.prepareStatement(pieQuery);
            ResultSet pieRs = pieStmt.executeQuery();
            while (pieRs.next()) {
                salaryDistributionChart.getData().add(new PieChart.Data(
                        pieRs.getString("department"),
                        pieRs.getDouble("total")
                ));
            }

            // Populate bar chart (department-wise payroll)
            String barQuery = "SELECT department, SUM(net_salary) AS total FROM payroll JOIN employees USING (employee_id) GROUP BY department";
            PreparedStatement barStmt = conn.prepareStatement(barQuery);
            ResultSet barRs = barStmt.executeQuery();
            XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
            while (barRs.next()) {
                barSeries.getData().add(new XYChart.Data<>(barRs.getString("department"), barRs.getDouble("total")));
            }
            payrollBarChart.getData().add(barSeries);

            // Populate line chart (payroll over time)
            String lineQuery = "SELECT pay_date, SUM(net_salary) AS total FROM payroll GROUP BY pay_date ORDER BY pay_date";
            PreparedStatement lineStmt = conn.prepareStatement(lineQuery);
            ResultSet lineRs = lineStmt.executeQuery();
            XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
            while (lineRs.next()) {
                lineSeries.getData().add(new XYChart.Data<>(lineRs.getString("pay_date"), lineRs.getDouble("total")));
            }
            payrollTrendChart.getData().add(lineSeries);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
