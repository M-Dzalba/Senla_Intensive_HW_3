package ru.dzalba.dao;

import org.springframework.stereotype.Component;
import ru.dzalba.models.Employee;
import ru.dzalba.utils.ConnectionHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeDao {

    private final ConnectionHolder connectionHolder;

    public EmployeeDao(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (id, fullName, birthDate, phoneNumber, email, positionId, departmentId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = connectionHolder.getConnection(true);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            System.out.println("Getting connection from addEmployee...");

            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getFullName());
            statement.setDate(3, new java.sql.Date(employee.getBirthDate().getTime()));
            statement.setString(4, employee.getPhoneNumber());
            statement.setString(5, employee.getEmail());
            statement.setInt(6, employee.getPositionId());
            statement.setInt(7, employee.getDepartmentId());
            int rowsAffected = statement.executeUpdate();
            System.out.println("addEmployee - Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add employee", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement in addEmployee: " + e.getMessage());
                }
            }
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        Connection connection = connectionHolder.getConnection(false);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("fullName"),
                        resultSet.getDate("birthDate"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"),
                        resultSet.getInt("positionId"),
                        resultSet.getInt("departmentId")
                ));
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet in getAllEmployees: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement in getAllEmployees: " + e.getMessage());
                }
            }
        }
        return employees;
    }

    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        Connection connection = connectionHolder.getConnection(false);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("fullName"),
                        resultSet.getDate("birthDate"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"),
                        resultSet.getInt("positionId"),
                        resultSet.getInt("departmentId")
                );
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet in getEmployeeById for employee id: "
                            + id + ". Error message: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement in getEmployeeById for employee id: "
                            + id + ". Error message: " + e.getMessage());
                }
            }
        }
        return null;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET fullName = ?, birthDate = ?, phoneNumber = ?, email = ?, positionId = ?, departmentId = ? WHERE id = ?";
        Connection connection = connectionHolder.getConnection(true);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            System.out.println("Getting connection from updateEmployee...");

            statement.setString(1, employee.getFullName());
            statement.setDate(2, new java.sql.Date(employee.getBirthDate().getTime()));
            statement.setString(3, employee.getPhoneNumber());
            statement.setString(4, employee.getEmail());
            statement.setInt(5, employee.getPositionId());
            statement.setInt(6, employee.getDepartmentId());
            statement.setInt(7, employee.getId());

            int rowsAffected = statement.executeUpdate();
            System.out.println("updateEmployee - Rows affected: " + rowsAffected);
            if (rowsAffected == 0) {
                System.out.println("No rows updated. Possible issue with ID: " + employee.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement in updateEmployee for employee id: "
                            + employee.getId() + ". Error message: " + e.getMessage());
                }
            }
        }
    }

    public void deleteEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        Connection connection = connectionHolder.getConnection(true);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            System.out.println("Getting connection from deleteEmployee...");

            statement.setInt(1, employeeId);
            int rowsAffected = statement.executeUpdate();
            System.out.println("deleteEmployee - Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete employee", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement in deleteEmployee for employee id: "
                            + employeeId + ". Error message: " + e.getMessage());
                }
            }
        }
    }
}