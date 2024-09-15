package ru.dzalba.service;

import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Department;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();

    void createEmployee(EmployeeDto employeeDTO);

    void updateEmployee(EmployeeDto employeeDTO);

    void deleteEmployee(int id);

    List<EmployeeDto> findEmployeesByName(String name);

    List<EmployeeDto> findEmployeesByDepartment(Department department);
}
