package com.example.payrollmanagementsystem.model;

import javafx.beans.property.*;

public class Employee {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty department;
    private final StringProperty position;
    private final DoubleProperty basicSalary;
    private final IntegerProperty workingHours;

    public Employee(String id, String name, String department, String position, double basicSalary, int workingHours) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.department = new SimpleStringProperty(department);
        this.position = new SimpleStringProperty(position);
        this.basicSalary = new SimpleDoubleProperty(basicSalary);
        this.workingHours = new SimpleIntegerProperty(workingHours);
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty positionProperty() { return position; }
    public DoubleProperty basicSalaryProperty() { return basicSalary; }
    public IntegerProperty workingHoursProperty() { return workingHours; }

    // Standard getters
    public String getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getDepartment() { return department.get(); }
    public String getPosition() { return position.get(); }
    public double getBasicSalary() { return basicSalary.get(); }
    public int getWorkingHours() { return workingHours.get(); }

    // Setters
    public void setId(String id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setDepartment(String department) { this.department.set(department); }
    public void setPosition(String position) { this.position.set(position); }
    public void setBasicSalary(double basicSalary) { this.basicSalary.set(basicSalary); }
    public void setWorkingHours(int workingHours) { this.workingHours.set(workingHours); }
}