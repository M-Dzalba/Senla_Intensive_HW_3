package ru.dzalba.service.seviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.PositionRepository;
import ru.dzalba.service.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createEmployee(EmployeeDto employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(EmployeeDto employeeDTO) {
        Optional<Employee> employee = Optional.of(convertToEntity(employeeDTO));
        employeeRepository.update(employee);
    }

    @Override
    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeDto> findEmployeesByName(String name) {
        List<Employee> employees = employeeRepository.findByName(name);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> findEmployeesByDepartment(Department department) {
        List<Employee> employees = employeeRepository.findByDepartment(department);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EmployeeDto convertToDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getFullName(),
                employee.getBirthDate(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                employee.getPosition().getId(),
                employee.getDepartment().getId()
        );
    }

    private Employee convertToEntity(EmployeeDto employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setFullName(employeeDTO.getFullName());
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setEmail(employeeDTO.getEmail());

        Position position = findPositionById(employeeDTO.getPositionId());
        Department department = findDepartmentById(employeeDTO.getDepartmentId());

        employee.setPosition(position);
        employee.setDepartment(department);

        return employee;
    }

    private Position findPositionById(int id) {
        Optional<Position> optionalPosition = positionRepository.findById(id);
        if (optionalPosition.isPresent()) {
            return optionalPosition.get();
        } else {
            throw new IllegalArgumentException("Position with ID " + id + " not found");
        }
    }

    private Department findDepartmentById(int id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            return optionalDepartment.get();
        } else {
            throw new IllegalArgumentException("Department with ID " + id + " not found");
        }
    }
}