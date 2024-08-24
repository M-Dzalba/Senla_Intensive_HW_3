package ru.dzalba.models;

public class Department {

    private int id;
    private String name;
    private String location;
    private Integer parentLocationId;

    public Department(int id, String name, String location, Integer parentLocationId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.parentLocationId = parentLocationId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Integer getParentLocationId() {
        return parentLocationId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setParentLocationId(Integer parentLocationId) {
        this.parentLocationId = parentLocationId;
    }
}
