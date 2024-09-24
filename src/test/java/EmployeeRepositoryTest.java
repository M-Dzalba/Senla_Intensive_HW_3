import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.PositionRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@SpringJUnitConfig(TestConfig.class)
@Transactional
@WebAppConfiguration
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void testSaveEmployee() {
        Position position = new Position("Developer", 50000);
        Department department = new Department("IT", "New York", 1);

        positionRepository.save(position);
        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFullName("John Doe");
        employee.setBirthDate(new Date());
        employee.setPhoneNumber("1234567890");
        employee.setEmail("john.doe@example.com");
        employee.setPosition(position);
        employee.setDepartment(department);

        employeeRepository.save(employee);

        Employee foundEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getFullName());
    }

    @Test
    public void testFindAllEmployees() {
        Position position1 = new Position("Developer", 50000);
        Position position2 = new Position("Manager", 80000);
        Department department1 = new Department("IT", "New York", 1);
        Department department2 = new Department("HR", "Chicago", 2);

        positionRepository.save(position1);
        positionRepository.save(position2);
        departmentRepository.save(department1);
        departmentRepository.save(department2);

        Employee employee1 = new Employee();
        employee1.setFullName("John Doe");
        employee1.setBirthDate(new Date());
        employee1.setPhoneNumber("1234567890");
        employee1.setEmail("john.doe@example.com");
        employee1.setPosition(position1);
        employee1.setDepartment(department1);

        Employee employee2 = new Employee();
        employee2.setFullName("Jane Smith");
        employee2.setBirthDate(new Date());
        employee2.setPhoneNumber("0987654321");
        employee2.setEmail("jane.smith@example.com");
        employee2.setPosition(position2);
        employee2.setDepartment(department2);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> allEmployees = employeeRepository.findAllWithDetails();
        assertNotNull(allEmployees);
        assertEquals(2, allEmployees.size());
    }

    @Test
    public void testUpdateEmployee() {
        Position position = new Position("Developer", 50000);
        Department department = new Department("IT", "New York", 1);

        positionRepository.save(position);
        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFullName("John Doe");
        employee.setBirthDate(new Date());
        employee.setPhoneNumber("1234567890");
        employee.setEmail("john.doe@example.com");
        employee.setPosition(position);
        employee.setDepartment(department);

        employeeRepository.save(employee);

        employee.setFullName("John Updated");
        employee.setPhoneNumber("1111111111");

        employeeRepository.save(employee);

        Employee updatedEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        assertNotNull(updatedEmployee);
        assertEquals("John Updated", updatedEmployee.getFullName());
        assertEquals("1111111111", updatedEmployee.getPhoneNumber());
    }

    @Test
    public void testDeleteEmployee() {
        Position position = new Position("Developer", 50000);
        Department department = new Department("IT", "New York", 1);

        positionRepository.save(position);
        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFullName("John Doe");
        employee.setBirthDate(new Date());
        employee.setPhoneNumber("1234567890");
        employee.setEmail("john.doe@example.com");
        employee.setPosition(position);
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);
        int employeeId = savedEmployee.getId();

        employeeRepository.delete(savedEmployee);

        assertThrows(NoSuchElementException.class, () -> {
            employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        });
    }
}