package com.example.roommate.annotations;

import com.example.roommate.RoomMateApplication;
import com.example.roommate.xcepto.controller.ControllerRouteSecurityConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@TestClass
@SpringBootTest(
        classes = RoomMateApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "spring.autoconfigure.exclude="
                        + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                        + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
                        + "org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration",
                "roommate.key-master-external=true",
                "spring.datasource.external=true"
        })
@Import(ControllerRouteSecurityConfig.class)
public @interface ControllerRouteTest {
}
