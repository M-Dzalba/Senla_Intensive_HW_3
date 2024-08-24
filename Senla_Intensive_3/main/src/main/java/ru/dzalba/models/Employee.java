package ru.dzalba.models;

import java.util.Date;

public class Employee {

    private int id;
    private String fullName;
    private Date birthDate;
    private String phoneNumber;
    private String email;
    private int positionId;
    private int departmentId;

    public Employee(int id, String fullName, Date birthDate, String phoneNumber, String email,
                    int positionId, int departmentId) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.positionId = positionId;
        this.departmentId = departmentId;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getPositionId() {
        return positionId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
