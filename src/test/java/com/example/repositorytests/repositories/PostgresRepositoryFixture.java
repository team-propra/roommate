package com.example.repositorytests.repositories;

import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.interfaces.repositories.IUserRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.UUID;

final class PostgresRepositoryFixture implements RepositoryFixture {
    private final PostgreSQLContainer postgres;
    private final String databaseName;
    private final ConfigurableApplicationContext context;

    PostgresRepositoryFixture() {
        postgres = SharedRepositoryTestContainers.postgres();
        databaseName = "test_" + UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT);
        createDatabase(databaseName);

        context = new SpringApplicationBuilder(PostgresRepositoryTestApplication.class)
                .web(WebApplicationType.NONE)
                .run(
                        "--spring.datasource.url=" + jdbcUrl(databaseName),
                        "--spring.datasource.username=" + postgres.getUsername(),
                        "--spring.datasource.password=" + postgres.getPassword(),
                        "--spring.datasource.driver-class-name=org.postgresql.Driver",
                        "--spring.flyway.enabled=true",
                        "--spring.main.banner-mode=off",
                        "--roommate.key-master-url=" + SharedRepositoryTestContainers.keymasterHost(),
                        "--roommate.key-master-port=" + SharedRepositoryTestContainers.keymasterPort()
                );
    }

    @Override
    public RepositoryBackend backend() {
        return RepositoryBackend.POSTGRES;
    }

    @Override
    public IRoomRepository rooms() {
        return context.getBean(IRoomRepository.class);
    }

    @Override
    public IItemRepository items() {
        return context.getBean(IItemRepository.class);
    }

    @Override
    public IUserRepository users() {
        return context.getBean(IUserRepository.class);
    }

    @Override
    public void close() {
        context.close();
        dropDatabase(databaseName);
    }

    private void createDatabase(String name) {
        executeAgainstDefaultDatabase("CREATE DATABASE " + name);
    }

    private void dropDatabase(String name) {
        executeAgainstDefaultDatabase("DROP DATABASE IF EXISTS " + name + " WITH (FORCE)");
    }

    private void executeAgainstDefaultDatabase(String sql) {
        try (
                var connection = DriverManager.getConnection(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                );
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to execute Postgres test database statement: " + sql, e);
        }
    }

    private String jdbcUrl(String database) {
        return "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/" + database;
    }
}
