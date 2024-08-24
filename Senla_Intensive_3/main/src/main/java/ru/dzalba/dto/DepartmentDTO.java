package ru.dzalba.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentDTO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("parentLocationId")
    private Integer parentLocationId;

    public DepartmentDTO(int id, String name, String location, Integer parentLocationId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.parentLocationId = parentLocationId;
    }

    public DepartmentDTO(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Integer parentLocationId) {
        this.parentLocationId = parentLocationId;
    }
}


