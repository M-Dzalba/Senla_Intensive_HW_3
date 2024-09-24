package ru.dzalba.service;

import ru.dzalba.dto.DepartmentDto;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentDto createDepartment(DepartmentDto departmentDTO);

    List<DepartmentDto> getAllDepartments();

    Optional<DepartmentDto> getDepartmentById(int id);

    Optional<DepartmentDto> updateDepartment(DepartmentDto departmentDTO);

    Optional<DepartmentDto> getDepartmentByName(String name);

    boolean deleteDepartment(int id);
}
