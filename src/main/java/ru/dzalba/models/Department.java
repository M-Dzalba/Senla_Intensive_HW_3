package ru.dzalba.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Department implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "parent_location_id")
    private Integer parentLocationId;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    public Department() {
    }

    public Department(String name, String location, Integer parentLocationId) {
        this.name = name;
        this.location = location;
        this.parentLocationId = parentLocationId;
    }

    public Integer getId() {
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
