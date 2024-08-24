package ru.dzalba;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.dzalba.config.ApplicationConfig;
import ru.dzalba.dto.*;
import ru.dzalba.service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@ComponentScan(basePackages = "ru.dzalba")

public class Application {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static ObjectMapper objectMapper;

    @Autowired
    public Application(ObjectMapper objectMapper) {
        Application.objectMapper = objectMapper;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        DepartmentService departmentService = context.getBean(DepartmentService.class);
        PositionService positionService = context.getBean(PositionService.class);
        EmployeeService employeeService = context.getBean(EmployeeService.class);
        ProjectService projectService = context.getBean(ProjectService.class);
        ProjectParticipationService participationService = context.getBean(ProjectParticipationService.class);

        System.out.println("Creating Departments...");
        DepartmentDTO department1 = new DepartmentDTO(1,"IT", "Building A");
        DepartmentDTO department2 = new DepartmentDTO(2,"HR", "Building B");

        departmentService.createDepartment(department1);
        departmentService.createDepartment(department2);

        System.out.println("Reading Department with ID 1...");
        DepartmentDTO readDepartment = departmentService.getDepartmentById(1).orElse(null);
        System.out.println("Read Department: " + toJson(readDepartment));

        System.out.println("Deleting Department with ID 2...");
        departmentService.deleteDepartment(2);

        System.out.println("Updating Department with ID 1...");
        department1.setLocation("Building C");
        departmentService.updateDepartment(department1);

        System.out.println("Reading Updated Department with ID 1...");
        DepartmentDTO updatedDepartment = departmentService.getDepartmentById(1).orElse(null);
        System.out.println("Updated Department: " + toJson(updatedDepartment));

        System.out.println("Creating Positions...");
        PositionDTO position1 = new PositionDTO(1,"Developer", 60000.00);
        PositionDTO position2 = new PositionDTO(2,"Manager", 80000.00);

        positionService.createPosition(position1);
        positionService.createPosition(position2);

        System.out.println("Reading Position with ID 1...");
        PositionDTO readPosition = positionService.getPositionById(1).orElse(null);
        System.out.println("Read Position: " + toJson(readPosition));

        System.out.println("Deleting Position with ID 2...");
        positionService.deletePosition(2);

        System.out.println("Updating Position with ID 1...");
        position1.setSalary(65000.00);
        positionService.updatePosition(position1);

        System.out.println("Reading Updated Position with ID 1...");
        PositionDTO updatedPosition = positionService.getPositionById(1).orElse(null);
        System.out.println("Updated Position: " + toJson(updatedPosition));

        System.out.println("Creating Employees...");
        EmployeeDTO employee1 = new EmployeeDTO(1,"Bob Bobson", parseDate("1990-01-01"), "555-1234", "bob@example.com", 1, 1);
        EmployeeDTO employee2 = new EmployeeDTO(2,"Ted Tedson", parseDate("1985-05-15"), "555-5678", "ted@example.com", 2, 2);

        employeeService.createEmployee(employee1);
        employeeService.createEmployee(employee2);

        System.out.println("Reading Employee with ID 1...");
        EmployeeDTO readEmployee = employeeService.getEmployeeById(1).orElse(null);
        System.out.println("Read Employee: " + toJson(readEmployee));

        System.out.println("Deleting Employee with ID 2...");
        employeeService.deleteEmployee(2);

        System.out.println("Updating Employee with ID 1...");
        employee1.setPhoneNumber("555-9999");
        employeeService.updateEmployee(employee1);

        System.out.println("Reading Updated Employee with ID 1...");
        EmployeeDTO updatedEmployee = employeeService.getEmployeeById(1).orElse(null);
        System.out.println("Updated Employee: " + toJson(updatedEmployee));

        System.out.println("Creating Projects...");
        ProjectDTO project1 = new ProjectDTO(1, "Project Alpha", "Description Alpha", parseDate("2024-01-01"), parseDate("2024-12-31"));
        ProjectDTO project2 = new ProjectDTO(2,"Project Beta", "Description Beta", parseDate("2024-02-01"), parseDate("2024-11-30"));

        projectService.createProject(project1);
        projectService.createProject(project2);

        System.out.println("Reading Project with ID 1...");
        ProjectDTO readProject = projectService.getProjectById(1).orElse(null);
        System.out.println("Read Project: " + toJson(readProject));

        System.out.println("Deleting Project with ID 2...");
        projectService.deleteProject(2);

        System.out.println("Updating Project with ID 1...");
        project1.setDescription("Updated description of Project Alpha");
        projectService.updateProject(project1);

        System.out.println("Reading Updated Project with ID 1...");
        ProjectDTO updatedProject = projectService.getProjectById(1).orElse(null);
        System.out.println("Updated Project: " + toJson(updatedProject));

        System.out.println("Adding Project Participation...");
        ProjectParticipationDTO participation1 = new ProjectParticipationDTO(1, 1, "Lead Developer", parseDate("2024-01-01"), parseDate("2024-12-31"));

        participationService.addParticipation(participation1);

        System.out.println("Reading Participation for Employee ID 1 and Project ID 1...");
        ProjectParticipationDTO readParticipation = participationService.getParticipation(1, 1).orElse(null);
        System.out.println("Read Participation: " + toJson(readParticipation));

        System.out.println("Updating Participation...");
        participation1.setRole("Senior Developer");
        participationService.updateParticipation(participation1);

        System.out.println("Reading Updated Participation...");
        ProjectParticipationDTO updatedParticipation = participationService.getParticipation(1, 1).orElse(null);
        System.out.println("Updated Participation: " + toJson(updatedParticipation));

        System.out.println("Deleting Participation...");
        participationService.deleteParticipation(1, 1);

        System.out.println("Reading Deleted Participation...");
        ProjectParticipationDTO deletedParticipation = participationService.getParticipation(1, 1).orElse(null);
        System.out.println("Deleted Participation: " + toJson(deletedParticipation));
        context.close();

    }

    private static Date parseDate(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date: " + dateString, e);
        }
    }

    private static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }
}