package ru.dzalba.service;

import org.springframework.stereotype.Service;
import ru.dzalba.annotations.Transaction;
import ru.dzalba.dao.EmployeeDao;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeDao employeeDAO;

    public EmployeeService(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Transaction
    public void createEmployee(EmployeeDto employeeDTO) {
        Employee employee = new Employee(
                employeeDTO.getId(),
                employeeDTO.getFullName(),
                employeeDTO.getBirthDate(),
                employeeDTO.getPhoneNumber(),
                employeeDTO.getEmail(),
                employeeDTO.getPositionId(),
                employeeDTO.getDepartmentId()
        );
        try {
            System.out.println("Executing addEmployee in EmployeeService...");
            employeeDAO.addEmployee(employee);
            System.out.println("addEmployee executed successfully.");
        } catch (SQLException e) {
            System.out.println("SQLException in createEmployee: " + e.getMessage());
            throw new RuntimeException("Failed to create employee", e);
        }
    }

    public List<EmployeeDto> getAllEmployees() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            return employees.stream()
                    .map(employee -> new EmployeeDto(
                            employee.getId(),
                            employee.getFullName(),
                            employee.getBirthDate(),
                            employee.getPhoneNumber(),
                            employee.getEmail(),
                            employee.getPositionId(),
                            employee.getDepartmentId()
                    ))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employees", e);
        }
    }

    @Transaction
    public void updateEmployee(EmployeeDto employeeDTO) {
        Employee employee = new Employee(
                employeeDTO.getId(),
                employeeDTO.getFullName(),
                employeeDTO.getBirthDate(),
                employeeDTO.getPhoneNumber(),
                employeeDTO.getEmail(),
                employeeDTO.getPositionId(),
                employeeDTO.getDepartmentId()
        );
        try {
            employeeDAO.updateEmployee(employee);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Transaction
    public void deleteEmployee(int id) {
        try {
            employeeDAO.deleteEmployee(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    public Optional<EmployeeDto> getEmployeeById(int id) {
        try {
            Employee employee = employeeDAO.getEmployeeById(id);
            return Optional.ofNullable(employee)
                    .map(e -> new EmployeeDto(
                            e.getId(),
                            e.getFullName(),
                            e.getBirthDate(),
                            e.getPhoneNumber(),
                            e.getEmail(),
                            e.getPositionId(),
                            e.getDepartmentId()
                    ));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee", e);
        }
    }
}