package com.example.roommate;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocalInfrastructureInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String POSTGRES_DATABASE = "postgres";
    private static final String POSTGRES_USERNAME = "postgres";
    private static final String POSTGRES_PASSWORD = "postgres";

    private GenericContainer<?> postgres;

    private GenericContainer<?> keymaster;

    private static GenericContainer<?> createPostgres() {
        return new GenericContainer<>(DockerImageName.parse("postgres:15"))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_DB", POSTGRES_DATABASE)
                .withEnv("POSTGRES_USER", POSTGRES_USERNAME)
                .withEnv("POSTGRES_PASSWORD", POSTGRES_PASSWORD);
    }

    private static GenericContainer<?> createKeymaster() {
        return new GenericContainer<>(
            new ImageFromDockerfile("roommate-keymaster-local", false)
                    .withFileFromPath("Dockerfile", Path.of("Dockerfile.keymaster"))
                    .withFileFromPath("keymaster.jar", Path.of("keymaster.jar"))
        )
                .withExposedPorts(3000)
                .withEnv("ROOMMATE_URL", "localhost")
                .withEnv("ROOMMATE_ENDPOINT", "/api/access");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> localProperties = new LinkedHashMap<>();

        if (!isExternalDatasource(environment)) {
            ensureDockerApiVersion();
            postgres = createPostgres();
            postgres.start();
            localProperties.put("spring.datasource.url", postgresJdbcUrl());
            localProperties.put("spring.datasource.username", POSTGRES_USERNAME);
            localProperties.put("spring.datasource.password", POSTGRES_PASSWORD);
            localProperties.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
        }

        if (!environment.getProperty("roommate.key-master-external", Boolean.class, false)) {
            ensureDockerApiVersion();
            keymaster = createKeymaster();
            keymaster.start();
            localProperties.put("roommate.key-master-url", keymaster.getHost());
            localProperties.put("roommate.key-master-port", keymaster.getMappedPort(3000));
        }

        if (!localProperties.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("localTestcontainersInfrastructure", localProperties));
            applicationContext.addApplicationListener(event -> {
                if (event instanceof ContextClosedEvent) {
                    this.stopLocalInfrastructure();
                }
            });
        }
    }

    private static boolean isExternalDatasource(ConfigurableEnvironment environment) {
        return environment.getProperty("spring.datasource.external", Boolean.class, false)
                || environment.getProperty("spring.datasource.exteral", Boolean.class, false);
    }

    private static void ensureDockerApiVersion() {
        if (System.getProperty("api.version") == null) {
            System.setProperty("api.version", "1.40");
        }
    }

    private void stopLocalInfrastructure() {
        if (keymaster != null && keymaster.isRunning()) {
            keymaster.stop();
        }
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }

    private String postgresJdbcUrl() {
        return "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/" + POSTGRES_DATABASE;
    }
}
