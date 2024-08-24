package ru.dzalba.dao;

import ru.dzalba.models.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDAO {

    private final List<Department> departments = new ArrayList<>();

    public void addDepartment(Department department) {
        departments.add(department);
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments);
    }

    public Optional<Department> getDepartmentById(int id) {
        return departments.stream()
                .filter(department -> department.getId() == id)
                .findFirst();
    }

    public void updateDepartment(Department department) {
        getDepartmentById(department.getId()).ifPresent(existingDepartment -> {
            existingDepartment.setName(department.getName());
            existingDepartment.setLocation(department.getLocation());
            existingDepartment.setParentLocationId(department.getParentLocationId());
        });
    }

    public void deleteDepartment(int id) {
        departments.removeIf(department -> department.getId() == id);
    }
}
