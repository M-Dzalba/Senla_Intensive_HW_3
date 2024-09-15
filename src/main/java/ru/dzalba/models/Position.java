package ru.dzalba.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "position")
public class Position implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "salary")
    private Double salary;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    public Position() {
    }

    public Position(String title, double salary) {
        this.title = title;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getSalary() {
        return salary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}