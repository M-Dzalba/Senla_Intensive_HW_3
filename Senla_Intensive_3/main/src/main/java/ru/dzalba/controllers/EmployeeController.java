package ru.dzalba.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.EmployeeDTO;
import ru.dzalba.service.EmployeeService;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/all")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/add")
    public String addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        java.util.Date birthDate = employeeDTO.getBirthDate();
        Date sqlBirthDate = Date.valueOf(String.valueOf(birthDate));

        employeeDTO.setBirthDate(sqlBirthDate);

        employeeService.createEmployee(employeeDTO);
        return "Employee added";
    }

    @PutMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, @RequestBody EmployeeDTO employeeDTO) {
        employeeDTO.setId(id);
        java.util.Date birthDate = employeeDTO.getBirthDate();
        Date sqlBirthDate = Date.valueOf(String.valueOf(birthDate));

        employeeDTO.setBirthDate(sqlBirthDate);

        employeeService.updateEmployee(employeeDTO);
        return "Employee updated";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted";
    }
}