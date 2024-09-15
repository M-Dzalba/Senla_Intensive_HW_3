package ru.dzalba.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.service.seviceImpl.EmployeeServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeServiceImpl.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeDto employeeDTO) {
        validateEmployeeDto(employeeDTO);

        employeeServiceImpl.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee added");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable int id, @RequestBody EmployeeDto employeeDTO) {
        validateEmployeeDto(employeeDTO);

        employeeDTO.setId(id);
        employeeServiceImpl.updateEmployee(employeeDTO);
        return ResponseEntity.ok("Employee updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        employeeServiceImpl.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted");
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<EmployeeDto>> findEmployeesByName(@RequestParam String name) {
        List<EmployeeDto> employees = employeeServiceImpl.findEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }

    private void validateEmployeeDto(EmployeeDto employeeDTO) {
        if (employeeDTO == null) {
            throw new IllegalArgumentException("EmployeeDto must not be null");
        }
        if (employeeDTO.getFullName() == null || employeeDTO.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name must not be null or empty");
        }
        if (employeeDTO.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date must not be null");
        }
        if (employeeDTO.getPositionId() == null) {
            throw new IllegalArgumentException("PositionId must not be null");
        }
        if (employeeDTO.getDepartmentId() == null) {
            throw new IllegalArgumentException("DepartmentId must not be null");
        }
    }
}