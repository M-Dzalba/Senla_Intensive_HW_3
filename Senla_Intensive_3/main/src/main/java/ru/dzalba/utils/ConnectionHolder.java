package ru.dzalba.utils;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Component
public class ConnectionHolder {

private final DataSource dataSource;
    private final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private final Map<Connection, Boolean> connectionMap = new HashMap<>();

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection(boolean transactional) throws SQLException {
        Connection connection = connectionThreadLocal.get();
        System.out.println("Thread ID: " + Thread.currentThread().getId());

        if (connection == null || connection.isClosed()) {
            System.out.println("Creating a new connection...");
            connection = dataSource.getConnection();
            connectionThreadLocal.set(connection);
            connectionMap.put(connection, transactional);
            if (transactional) {
                connection.setAutoCommit(false);
            }
        }
        return connection;
    }

    public void beginTransaction() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && connectionMap.get(connection)) {
            System.out.println("Starting transaction...");
            connection.setAutoCommit(false);
        }
    }

    public void commit() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && connectionMap.get(connection)) {
            connection.commit();
        }
    }

    public void rollback() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && connectionMap.get(connection)) {
            connection.rollback();
        }
    }

    public void closeConnection() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            connectionThreadLocal.remove();
            if (!connection.isClosed()) {
                connection.close();
            }
            connectionMap.remove(connection);
        }
    }


}
