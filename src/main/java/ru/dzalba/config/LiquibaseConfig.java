package ru.dzalba.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@Profile("!test")
public class LiquibaseConfig {

    @Value("${liquibase.change-log}")
    private String changeLogFile;

    private final DataSource dataSource;

    public LiquibaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> liquibaseInitializer() {
        return event -> {
            try (Connection connection = dataSource.getConnection()) {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                Liquibase liquibase = new Liquibase(changeLogFile,
                        new ClassLoaderResourceAccessor(), database);

                liquibase.update("");
            } catch (SQLException | LiquibaseException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to execute Liquibase migrations", e);
            }
        };
    }
}