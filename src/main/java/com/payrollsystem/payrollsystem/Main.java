package com.payrollsystem.payrollsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.Objects;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/payroll system/views/login_view.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Payroll Management System");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
