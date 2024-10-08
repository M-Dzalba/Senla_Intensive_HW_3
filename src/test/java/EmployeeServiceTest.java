import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.PositionRepository;
import ru.dzalba.service.seviceImpl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeDto employeeDto;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        Position position = new Position();
        position.setId(1);
        position.setTitle("Developer");
        position.setSalary(100);

        Department department = new Department();
        department.setId(1);
        department.setName("IT");
        department.setLocation("Brest");
        department.setParentLocationId(2);

        employeeDto = new EmployeeDto(1, "John Doe", null, null, null, 1, 1);
        employee = new Employee();
        employee.setId(1);
        employee.setFullName("John Doe");
        employee.setPosition(position);
        employee.setDepartment(department);

        lenient().when(positionRepository.findById(1)).thenReturn(Optional.of(position));
        lenient().when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        lenient().when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

    }

    @Test
    public void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testCreateEmployee() {
        employeeService.createEmployee(employeeDto);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() {

        employeeService.updateEmployee(employeeDto);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1)).delete(any(Employee.class));
    }

    @Test
    public void testFindEmployeesByName() {
        when(employeeRepository.findByName("John Doe")).thenReturn(Collections.singletonList(employee));

        List<EmployeeDto> result = employeeService.findEmployeesByName("John Doe");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        verify(employeeRepository, times(1)).findByName("John Doe");
    }

    @Test
    public void testFindById() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Optional<EmployeeDto> result = employeeService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getFullName());
        verify(employeeRepository, times(1)).findById(1);
    }
}