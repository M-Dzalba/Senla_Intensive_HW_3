package ru.dzalba.models;

public class Position {

    private int id;
    private String title;
    private double salary;

    public Position(int id, String title, double salary) {
        this.id = id;
        this.title = title;
        this.salary = salary;
    }

    public int getId() {
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
