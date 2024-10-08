import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.dzalba.config.SecurityConfig;
import ru.dzalba.controllers.EmployeeController;
import ru.dzalba.dto.EmployeeDto;
import ru.dzalba.models.Department;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Position;
import ru.dzalba.repository.DepartmentRepository;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.PositionRepository;
import ru.dzalba.security.JwtAuthenticationFilter;
import ru.dzalba.security.JwtAuthorizationFilter;
import ru.dzalba.utils.JwtTokenProvider;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, SecurityConfig.class, EmployeeController.class})
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private String validToken;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider))
                .addFilters(new JwtAuthorizationFilter(authenticationManager, jwtTokenProvider))
                .build();

        objectMapper = new ObjectMapper();

        validToken = jwtTokenProvider.generateToken("admin", "ADMIN");
        System.out.println("Generated Token: " + validToken);

        createTestData();
    }

    private void createTestData() {
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
    public void testValidateToken() {
        assertFalse(validToken.isEmpty(), "Token should not be empty");
        boolean isValid = jwtTokenProvider.validateToken(validToken, "admin");
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    public void testCreateToken() {
        String token = jwtTokenProvider.generateToken("admin", "ADMIN");
        assertNotNull(token, "Token should not be null");
        boolean isValid = jwtTokenProvider.validateToken(token, "admin");
        assertTrue(isValid, "Token should be valid for the username");
    }

    @Test
    public void testAccessWithInvalidToken() throws Exception {
        String invalidToken = "Bearer.invalid.token";

        mockMvc.perform(get("/employees")
                        .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized access. Please log in."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanAddEmployee() throws Exception {
        String employeeJson = "{ \"fullName\": \"Jane Doe\", \"birthDate\": \"1990-01-01\", \"positionId\": 1, \"departmentId\": 1 }";

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserCannotAddEmployee() throws Exception {
        String employeeJson = "{ \"fullName\": \"Jane Doe\", \"birthDate\": \"1990-01-01\", \"positionId\": 1, \"departmentId\": 1 }";
        validToken = jwtTokenProvider.generateToken("user", "USER");
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You do not have permission to access this resource."));
    }

    @Test
    @Transactional
    @WithMockUser(roles = "USER")
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees")
                        .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    public void testCreateEmployee() throws Exception {
        String employeeJson = "{\"fullName\":\"John Doe\", \"birthDate\":\"1990-01-01\", \"phoneNumber\":\"123456789\", \"email\":\"john@example.com\", \"positionId\":1, \"departmentId\":1}";

        mockMvc.perform(post("/employees")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated());

        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(1).getFullName());
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
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
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployeeDto)))
                .andReturn();

        Employee retrievedEmployee = employeeRepository.findById(updatedEmployeeDto.getId()).orElseThrow();
        assertEquals("John Doe Updated", retrievedEmployee.getFullName());
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employees/1")
                        .header("Authorization", validToken))
                .andExpect(status().isOk());

        assertFalse(employeeRepository.findById(1).isPresent());
    }
}