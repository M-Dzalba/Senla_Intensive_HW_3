package ru.dzalba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.DepartmentDto;
import ru.dzalba.service.seviceImpl.DepartmentServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentServiceImpl departmentServiceImpl;
    private final ObjectMapper objectMapper;

    public DepartmentController(DepartmentServiceImpl departmentServiceImpl, ObjectMapper objectMapper) {
        this.departmentServiceImpl = departmentServiceImpl;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDto departmentDTO) {
        DepartmentDto createdDepartment = departmentServiceImpl.createDepartment(departmentDTO);
        try {
            String json = objectMapper.writeValueAsString(createdDepartment);
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllDepartments() {
        List<DepartmentDto> departments = departmentServiceImpl.getAllDepartments();
        try {
            String json = objectMapper.writeValueAsString(departments);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getDepartmentById(@PathVariable int id) {
        Optional<DepartmentDto> department = departmentServiceImpl.getDepartmentById(id);
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
    public ResponseEntity<String> updateDepartment(@PathVariable int id, @RequestBody DepartmentDto departmentDTO) {
        if (departmentServiceImpl.getDepartmentById(id).isPresent()) {
            departmentDTO.setId(id);
            Optional<DepartmentDto> updatedDepartment = departmentServiceImpl.updateDepartment(departmentDTO);
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
        if (departmentServiceImpl.deleteDepartment(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}