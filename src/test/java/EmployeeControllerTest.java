import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.PositionRepository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        objectMapper = new ObjectMapper();

        Position position = new Position();
        position.setId(1);
        position.setTitle("Developer");
        position.setSalary(100);
        positionRepository.save(position);

        Department department = new Department();
        department.setId(1);
        department.setName("IT");
        department.setLocation("Brest");
        department.setParentLocationId(2);
        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setId(1);
        employee.setFullName("John Doe");
        employee.setBirthDate(new Date());
        employee.setPosition(position);
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }

    @Test
    @Transactional
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    @Transactional
    public void testCreateEmployee() throws Exception {
        String employeeJson = "{\"fullName\":\"John Doe\", \"birthDate\":\"1990-01-01\", \"phoneNumber\":\"123456789\", \"email\":\"john@example.com\", \"positionId\":1, \"departmentId\":1}";

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated());


        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(1).getFullName());
    }

    @Test
    @Transactional
    public void testUpdateEmployee() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = dateFormat.parse("1990-01-01");

        EmployeeDto updatedEmployeeDto = new EmployeeDto();
        updatedEmployeeDto.setId(1);
        updatedEmployeeDto.setFullName("John Doe Updated");
        updatedEmployeeDto.setBirthDate(birthDate);
        updatedEmployeeDto.setPositionId(1);
        updatedEmployeeDto.setDepartmentId(1);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployeeDto)))
                .andReturn();

        Employee retrievedEmployee = employeeRepository.findById(updatedEmployeeDto.getId()).orElseThrow();
        assertEquals("John Doe Updated", retrievedEmployee.getFullName());
    }

    @Test
    @Transactional
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Employee deleted\""));

        assertFalse(employeeRepository.findById(1).isPresent());
    }
}