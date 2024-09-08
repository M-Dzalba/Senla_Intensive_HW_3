package ru.dzalba.service;

import org.springframework.stereotype.Service;
import ru.dzalba.dao.DepartmentDao;
import ru.dzalba.dto.DepartmentDto;
import ru.dzalba.models.Department;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final DepartmentDao departmentDAO;

    public DepartmentService(DepartmentDao departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public DepartmentDto createDepartment(DepartmentDto departmentDTO) {
        Department department = new Department(departmentDTO.getId(), departmentDTO.getName(),
                departmentDTO.getLocation(), departmentDTO.getParentLocationId());
        departmentDAO.addDepartment(department);
        return departmentDTO;
    }

    public List<DepartmentDto> getAllDepartments() {
        return departmentDAO.getAllDepartments().stream()
                .map(department -> new DepartmentDto(department.getId(), department.getName(),
                        department.getLocation(), department.getParentLocationId()))
                .collect(Collectors.toList());
    }

    public Optional<DepartmentDto> getDepartmentById(int id) {
        return departmentDAO.getDepartmentById(id)
                .map(department -> new DepartmentDto(department.getId(), department.getName(),
                        department.getLocation(), department.getParentLocationId()));
    }

    public Optional<DepartmentDto> updateDepartment(DepartmentDto departmentDTO) {
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
