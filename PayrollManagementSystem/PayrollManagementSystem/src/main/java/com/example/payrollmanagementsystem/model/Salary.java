package com.example.payrollmanagementsystem.model;

import javafx.beans.property.*;

public class Salary {
    private final StringProperty employeeId;
    private final DoubleProperty basicSalary;
    private final DoubleProperty overtimePay;
    private final DoubleProperty allowances;
    private final DoubleProperty tax;
    private final DoubleProperty deductions;
    private final DoubleProperty netSalary;
    private final ObjectProperty<Employee> employee;

    public Salary(Employee employee, double basicSalary, double overtimePay, double allowances,
                  double tax, double deductions, double netSalary) {
        this.employee = new SimpleObjectProperty<>(employee);
        this.employeeId = new SimpleStringProperty(employee.getId());
        this.basicSalary = new SimpleDoubleProperty(basicSalary);
        this.overtimePay = new SimpleDoubleProperty(overtimePay);
        this.allowances = new SimpleDoubleProperty(allowances);
        this.tax = new SimpleDoubleProperty(tax);
        this.deductions = new SimpleDoubleProperty(deductions);
        this.netSalary = new SimpleDoubleProperty(netSalary);
    }

    // Property getters
    public ObjectProperty<Employee> employeeProperty() { return employee; }
    public StringProperty employeeIdProperty() { return employeeId; }
    public DoubleProperty basicSalaryProperty() { return basicSalary; }
    public DoubleProperty overtimePayProperty() { return overtimePay; }
    public DoubleProperty allowancesProperty() { return allowances; }
    public DoubleProperty taxProperty() { return tax; }
    public DoubleProperty deductionsProperty() { return deductions; }
    public DoubleProperty netSalaryProperty() { return netSalary; }

    // Standard getters
    public Employee getEmployee() { return employee.get(); }
    public String getEmployeeId() { return employeeId.get(); }
    public double getBasicSalary() { return basicSalary.get(); }
    public double getOvertimePay() { return overtimePay.get(); }
    public double getAllowances() { return allowances.get(); }
    public double getTax() { return tax.get(); }
    public double getDeductions() { return deductions.get(); }
    public double getNetSalary() { return netSalary.get(); }

    // Setters
    public void setEmployee(Employee employee) { this.employee.set(employee); }
    public void setEmployeeId(String employeeId) { this.employeeId.set(employeeId); }
    public void setBasicSalary(double basicSalary) { this.basicSalary.set(basicSalary); }
    public void setOvertimePay(double overtimePay) { this.overtimePay.set(overtimePay); }
    public void setAllowances(double allowances) { this.allowances.set(allowances); }
    public void setTax(double tax) { this.tax.set(tax); }
    public void setDeductions(double deductions) { this.deductions.set(deductions); }
    public void setNetSalary(double netSalary) { this.netSalary.set(netSalary); }
}