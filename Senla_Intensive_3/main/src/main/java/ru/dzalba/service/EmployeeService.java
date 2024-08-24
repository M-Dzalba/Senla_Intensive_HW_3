package ru.dzalba.service;

import org.springframework.stereotype.Service;
import ru.dzalba.dao.EmployeeDAO;
import ru.dzalba.dto.EmployeeDTO;
import ru.dzalba.models.Employee;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getId(), employeeDTO.getFullName(), employeeDTO.getBirthDate(),
                employeeDTO.getPhoneNumber(), employeeDTO.getEmail(), employeeDTO.getPositionId(), employeeDTO.getDepartmentId());
        employeeDAO.addEmployee(employee);
        return employeeDTO;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeDAO.getAllEmployees().stream()
                .map(employee -> new EmployeeDTO(employee.getId(), employee.getFullName(), employee.getBirthDate(),
                        employee.getPhoneNumber(), employee.getEmail(), employee.getPositionId(), employee.getDepartmentId()))
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> getEmployeeById(int id) {
        return employeeDAO.getEmployeeById(id)
                .map(employee -> new EmployeeDTO(employee.getId(), employee.getFullName(), employee.getBirthDate(),
                        employee.getPhoneNumber(), employee.getEmail(), employee.getPositionId(), employee.getDepartmentId()));
    }

    public Optional<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getId(), employeeDTO.getFullName(), employeeDTO.getBirthDate(),
                employeeDTO.getPhoneNumber(), employeeDTO.getEmail(), employeeDTO.getPositionId(), employeeDTO.getDepartmentId());
        employeeDAO.updateEmployee(employee);
        return Optional.of(employeeDTO);
    }

    public boolean deleteEmployee(int id) {
        Optional<Employee> employee = employeeDAO.getEmployeeById(id);
        if (employee.isPresent()) {
            employeeDAO.deleteEmployee(id);
            return true;
        }
        return false;
    }
}
