package ru.dzalba.utils;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ConnectionHolder {

    private final DataSource dataSource;
    private final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Boolean> transactionThreadLocal = new ThreadLocal<>();
    private final Map<Connection, Boolean> connectionMap = new HashMap<>();

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection(boolean transactional) throws SQLException {
        Connection connection = connectionThreadLocal.get();
        Boolean isCurrentTransactional = transactionThreadLocal.get();

        if (connection == null || connection.isClosed() || (transactional && !isCurrentTransactional)) {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connectionMap.remove(connection);
            }

            if (!transactional) {
                Optional<Connection> freeConnection = connectionMap.entrySet().stream()
                        .filter(entry -> !entry.getValue())
                        .map(Map.Entry::getKey)
                        .findFirst();

                if (freeConnection.isPresent()) {
                    connection = freeConnection.get();
                    connectionThreadLocal.set(connection);
                    transactionThreadLocal.set(false);
                    return connection;
                }
            }

            connection = dataSource.getConnection();
            connectionThreadLocal.set(connection);
            transactionThreadLocal.set(transactional);
            connectionMap.put(connection, transactional);
        } else if (transactional && !isCurrentTransactional) {
            throw new IllegalStateException("Cannot reuse a non-transactional connection for a transactional operation.");
        }

        return connection;
    }

    public void beginTransaction() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && transactionThreadLocal.get()) {
            connection.setAutoCommit(false);
        } else {
            throw new IllegalStateException("No transactional connection found to start transaction.");
        }
    }

    public void commit() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && transactionThreadLocal.get()) {
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Transaction committed.");
        } else {
            throw new IllegalStateException("No transactional connection found to commit.");
        }
    }

    public void rollback() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && transactionThreadLocal.get()) {
            connection.rollback();
            connection.setAutoCommit(true);
            System.out.println("Transaction rolled back.");
        } else {
            throw new IllegalStateException("No transactional connection found to rollback.");
        }
    }

    public void closeConnection() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            if (!connection.isClosed()) {
                if (transactionThreadLocal.get() != null && transactionThreadLocal.get()) {
                    throw new IllegalStateException("Transactional connection cannot be closed before commit/rollback.");
                }
                connection.close();
                System.out.println("Connection closed.");
            }
            connectionThreadLocal.remove();
            transactionThreadLocal.remove();
            connectionMap.remove(connection);
        }
    }
}
