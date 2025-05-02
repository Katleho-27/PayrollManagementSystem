module com.example.payrollmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.payrollmanagementsystem to javafx.fxml;
    exports com.example.payrollmanagementsystem;
    exports com.example.payrollmanagementsystem.controller;
    opens com.example.payrollmanagementsystem.controller to javafx.fxml;
    exports com.example.payrollmanagementsystem.model;
    opens com.example.payrollmanagementsystem.model to javafx.fxml;
}