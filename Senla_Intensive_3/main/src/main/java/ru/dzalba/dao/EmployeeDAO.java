package ru.dzalba.dao;

import org.springframework.stereotype.Component;
import ru.dzalba.models.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDAO {
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Optional<Employee> getEmployeeById(int id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst();
    }

    public void updateEmployee(Employee employee) {
        getEmployeeById(employee.getId()).ifPresent(existingEmployee -> {
            existingEmployee.setFullName(employee.getFullName());
            existingEmployee.setBirthDate(employee.getBirthDate());
            existingEmployee.setPhoneNumber(employee.getPhoneNumber());
            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setPositionId(employee.getPositionId());
            existingEmployee.setDepartmentId(employee.getDepartmentId());
        });
    }

    public void deleteEmployee(int id) {
        employees.removeIf(employee -> employee.getId() == id);
    }
}