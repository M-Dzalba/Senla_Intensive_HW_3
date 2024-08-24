package ru.dzalba.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ProjectParticipationDTO {
    @JsonProperty("employeeId")
    private int employeeId;

    @JsonProperty("projectId")
    private int projectId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    public ProjectParticipationDTO(int employeeId, int projectId, String role,
                                   Date startDate, Date endDate) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}