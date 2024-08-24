package ru.dzalba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.DepartmentDTO;
import ru.dzalba.service.DepartmentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final ObjectMapper objectMapper;

    public DepartmentController(DepartmentService departmentService, ObjectMapper objectMapper) {
        this.departmentService = departmentService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        try {
            String json = objectMapper.writeValueAsString(createdDepartment);
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        try {
            String json = objectMapper.writeValueAsString(departments);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getDepartmentById(@PathVariable int id) {
        Optional<DepartmentDTO> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            try {
                String json = objectMapper.writeValueAsString(department.get());
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable int id, @RequestBody DepartmentDTO departmentDTO) {
        if (departmentService.getDepartmentById(id).isPresent()) {
            departmentDTO.setId(id);
            Optional<DepartmentDTO> updatedDepartment = departmentService.updateDepartment(departmentDTO);
            if (updatedDepartment.isPresent()) {
                try {
                    String json = objectMapper.writeValueAsString(updatedDepartment.get());
                    return new ResponseEntity<>(json, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        if (departmentService.deleteDepartment(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
