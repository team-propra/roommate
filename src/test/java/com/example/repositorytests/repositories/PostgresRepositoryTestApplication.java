package com.example.repositorytests.repositories;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJdbcRepositories(basePackages = "com.example.roommate.persistence.postgres")
@ComponentScan(basePackages = "com.example.roommate.persistence.postgres")
public class PostgresRepositoryTestApplication {
}
