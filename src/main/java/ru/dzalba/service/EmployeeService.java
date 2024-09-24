package ru.dzalba.service;

import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();

    void createEmployee(EmployeeDto employeeDTO);

    Employee updateEmployee(EmployeeDto employeeDTO);

    void deleteEmployee(int id);

    List<EmployeeDto> findEmployeesByName(String name);

    List<EmployeeDto> findEmployeesByDepartment(Department department);

    Optional<EmployeeDto> findById(int id);
}
