package ru.dzalba.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("salary")
    private double salary;

    public PositionDTO(int id, String title, double salary) {
        this.id = id;
        this.title = title;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
