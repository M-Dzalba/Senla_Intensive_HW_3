package ru.dzalba;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.dzalba.config.ApplicationConfig;
import ru.dzalba.dao.EmployeeDao;
import ru.dzalba.dto.*;
import ru.dzalba.service.*;
import ru.dzalba.utils.ConnectionHolder;

import javax.sql.DataSource;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@ComponentScan(basePackages = "ru.dzalba")
public class Application {

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private static final Random random = new Random();
    private static ObjectMapper objectMapper;

    @Autowired
    public Application(ObjectMapper objectMapper) {
        Application.objectMapper = objectMapper;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        DataSource dataSource = context.getBean(DataSource.class);
        EmployeeService employeeService = new EmployeeService(new EmployeeDao(new ConnectionHolder(dataSource)));
        AtomicInteger randomId1 = new AtomicInteger();
        AtomicInteger randomId2 = new AtomicInteger();

        executor.execute(() -> {
            try {
                randomId1.set(random.nextInt(100));
                employeeService.createEmployee(new EmployeeDto(randomId1.get(), "John First", new Date(), "1234567890", "john.first@example.com", 1, 1));
                System.out.println("Employee 1 with ID " + randomId1 + " created.");
                showEmployee(employeeService, randomId1.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                randomId2.set(random.nextInt(100));
                employeeService.createEmployee(new EmployeeDto(randomId2.get(), "Jane Smith", new Date(), "0987654321", "jane.smith@example.com", 2, 2));
                System.out.println("Employee 2 with ID " + randomId2 + " created.");
                showEmployee(employeeService, randomId2.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                Thread.sleep(1000);
                int employeeIdToUpdate = randomId1.get();
                EmployeeDto updatedEmployee = new EmployeeDto(employeeIdToUpdate, "John First-Updated", new Date(), "1234567890", "john.updated@example.com", 1, 1);
                employeeService.updateEmployee(updatedEmployee);
                System.out.println("Employee with ID " + employeeIdToUpdate + " updated.");
                showEmployee(employeeService, employeeIdToUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                Thread.sleep(1000);
                int employeeIdToDelete = randomId2.get();
                employeeService.deleteEmployee(employeeIdToDelete);
                System.out.println("Employee with ID " + employeeIdToDelete + " deleted.");
                showEmployee(employeeService, employeeIdToDelete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        context.close();
    }

    private static void showEmployee(EmployeeService employeeService, int id) {
        Optional<EmployeeDto> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            System.out.println("Employee details: " + toJson(employee.get()));
        } else {
            System.out.println("Employee with ID " + id + " not found.");
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