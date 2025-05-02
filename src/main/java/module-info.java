module com.payrollsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.payrollsystem.payrollsystem to javafx.fxml;
    opens controllers to javafx.fxml;
    opens models to javafx.base;

    exports com.payrollsystem.payrollsystem;
    exports controllers;
}
