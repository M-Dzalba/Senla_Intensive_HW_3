package ru.dzalba.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(required = false) String name) {
        List<EmployeeDto> employees = (name != null) ? employeeService.findEmployeesByName(name) : employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeDto employeeDTO) {
        validateEmployeeDto(employeeDTO);
        employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee added");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDto employeeDTO) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Invalid employee ID");
        }
        validateEmployeeDto(employeeDTO);
        employeeDTO.setId(id);
        employeeService.updateEmployee(employeeDTO);
        return ResponseEntity.ok("Employee updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Invalid employee ID");
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted");
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<EmployeeDto>> findEmployeesByName(@RequestParam(required = false) String name) {
        List<EmployeeDto> employees = (name != null) ? employeeService.findEmployeesByName(name) : employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<EmployeeDto> employeeDto = employeeService.findById(id);
        return employeeDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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