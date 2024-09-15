package ru.dzalba.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class EmployeeDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("birthDate")
    private Date birthDate;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("positionId")
    private Integer positionId;

    @JsonProperty("departmentId")
    private Integer departmentId;

    public EmployeeDto(int id, String fullName, Date birthDate, String phoneNumber, String email,
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

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}