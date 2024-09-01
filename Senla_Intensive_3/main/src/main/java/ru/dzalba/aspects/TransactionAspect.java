package ru.dzalba.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dzalba.utils.ConnectionHolder;

import java.sql.Connection;
import java.sql.SQLException;

@Aspect
@Component
public class TransactionAspect {

    @Autowired
    private ConnectionHolder connectionHolder;

    @Around("@annotation(ru.dzalba.annotations.Transaction)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Connection connection = null;

        try {
            connection = connectionHolder.getConnection(true);
            System.out.println("Getting connection from TransactionAspect...");

            connectionHolder.beginTransaction();

            Object result = joinPoint.proceed();

            System.out.println("Committing transaction...");
            connectionHolder.commit();
            System.out.println("Transaction committed.");

            return result;
        } catch (SQLException | RuntimeException e) {
            System.out.println("Exception in TransactionAspect: " + e.getMessage());
            if (connection != null) {
                System.out.println("Rolling back transaction...");
                connectionHolder.rollback();
                System.out.println("Transaction rolled back.");
            }
            throw e;
        } finally {
            if (connection != null) {
                System.out.println("Closing connection...");
                connectionHolder.closeConnection();
                System.out.println("Connection closed.");
            }
        }
    }
}