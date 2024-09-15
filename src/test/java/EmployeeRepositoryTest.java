import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.PositionRepository;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;


import java.util.Date;

import static org.junit.Assert.*;

@SpringJUnitConfig(TestConfig.class)
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

        Employee foundEmployee = employeeRepository.findById(employee.getId());
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getFullName());
    }
}