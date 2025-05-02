package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/payroll_system";
    private static final String USER = "root";
    private static final String PASSWORD = "901017360"; // <- replace with your MySQL password

    private static Connection connection;

    // Get a singleton connection instance
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load JDBC driver (optional in modern Java, but good practice)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connected to the database.");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ MySQL JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("❌ Database connection failed.");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
