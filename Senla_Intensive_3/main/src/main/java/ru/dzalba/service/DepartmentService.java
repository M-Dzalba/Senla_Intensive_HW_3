package ru.dzalba.service;

import ru.dzalba.dao.DepartmentDAO;
import ru.dzalba.dto.DepartmentDTO;
import ru.dzalba.models.Department;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepartmentService {

    private final DepartmentDAO departmentDAO;

    public DepartmentService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = new Department(departmentDTO.getId(), departmentDTO.getName(),
                departmentDTO.getLocation(), departmentDTO.getParentLocationId());
        departmentDAO.addDepartment(department);
        return departmentDTO;
    }

    public List<DepartmentDTO> getAllDepartments() {
        return departmentDAO.getAllDepartments().stream()
                .map(department -> new DepartmentDTO(department.getId(), department.getName(),
                        department.getLocation(), department.getParentLocationId()))
                .collect(Collectors.toList());
    }

    public Optional<DepartmentDTO> getDepartmentById(int id) {
        return departmentDAO.getDepartmentById(id)
                .map(department -> new DepartmentDTO(department.getId(), department.getName(),
                        department.getLocation(), department.getParentLocationId()));
    }

    public Optional<DepartmentDTO> updateDepartment(DepartmentDTO departmentDTO) {
        Department department = new Department(departmentDTO.getId(), departmentDTO.getName(),
                departmentDTO.getLocation(), departmentDTO.getParentLocationId());
        departmentDAO.updateDepartment(department);
        return Optional.of(departmentDTO);
    }

    public boolean deleteDepartment(int id) {
        Optional<Department> department = departmentDAO.getDepartmentById(id);
        if (department.isPresent()) {
            departmentDAO.deleteDepartment(id);
            return true;
        }
        return false;
    }
}
