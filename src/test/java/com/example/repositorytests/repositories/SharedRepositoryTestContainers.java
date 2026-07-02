package com.example.repositorytests.repositories;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;

final class SharedRepositoryTestContainers {
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
            .withDatabaseName("postgres")
            .withUsername("test")
            .withPassword("test");

    private static final GenericContainer<?> KEYMASTER = new GenericContainer<>(
            new ImageFromDockerfile("roommate-keymaster-test", false)
                    .withFileFromPath("Dockerfile", Path.of("Dockerfile.keymaster"))
                    .withFileFromPath("keymaster.jar", Path.of("keymaster.jar"))
    )
            .withExposedPorts(3000)
            .withEnv("ROOMMATE_URL", "localhost")
            .withEnv("ROOMMATE_ENDPOINT", "/key");

    private static boolean started;

    private SharedRepositoryTestContainers() {
    }

    static synchronized PostgreSQLContainer<?> postgres() {
        start();
        return POSTGRES;
    }

    static synchronized String keymasterHost() {
        start();
        return KEYMASTER.getHost();
    }

    static synchronized int keymasterPort() {
        start();
        return KEYMASTER.getMappedPort(3000);
    }

    private static void start() {
        if (started) {
            return;
        }
        POSTGRES.start();
        KEYMASTER.start();
        started = true;
    }
}
