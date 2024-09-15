package ru.dzalba.service.seviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dzalba.dto.DepartmentDto;
import ru.dzalba.models.Department;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.service.DepartmentService;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = Objects.requireNonNull(departmentRepository, "DepartmentRepository must not be null");
    }
    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDTO) {
        if (departmentDTO == null) {
            throw new IllegalArgumentException("DepartmentDto must not be null");
        }

        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setLocation(departmentDTO.getLocation());
        department.setParentLocationId(departmentDTO.getParentLocationId());
        departmentRepository.save(department);
        return departmentDTO;
    }
    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        if (departments == null) {
            return Collections.emptyList();
        }
        return departments.stream()
                .map(department -> new DepartmentDto(department.getId(), department.getName(),
                        department.getLocation(), department.getParentLocationId()))
                .collect(Collectors.toList());
    }
    @Override
    public Optional<DepartmentDto> getDepartmentById(int id) {
        validateId(id);
        return Optional.ofNullable(departmentRepository.findById(id))
                .map(department -> new DepartmentDto(department.getId(), department.getName(),
                        department.getLocation(), department.getParentLocationId()));
    }
    @Override
    public Optional<DepartmentDto> updateDepartment(DepartmentDto departmentDTO) {
        if (departmentDTO == null) {
            throw new IllegalArgumentException("DepartmentDto must not be null");
        }

        validateId(departmentDTO.getId());
        Department department = departmentRepository.findById(departmentDTO.getId());
        if (department == null) {
            return Optional.empty();
        }
        department.setName(departmentDTO.getName());
        department.setLocation(departmentDTO.getLocation());
        department.setParentLocationId(departmentDTO.getParentLocationId());
        departmentRepository.update(department);
        return Optional.of(departmentDTO);
    }
    @Override
    public Optional<DepartmentDto> getDepartmentByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name must not be null or empty");
        }

        try {
            Department department = departmentRepository.findByName(name);
            return Optional.of(new DepartmentDto(department.getId(), department.getName(),
                    department.getLocation(), department.getParentLocationId()));
        } catch (NoResultException e) {

            return Optional.empty();
        }
    }
    @Override
    public boolean deleteDepartment(int id) {
        validateId(id);
        Department department = departmentRepository.findById(id);
        if (department != null) {
            departmentRepository.delete(department);
            return true;
        }
        return false;
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than zero");
        }
    }
}
