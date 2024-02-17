package com.example.roommate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.roommate")
@EnableScheduling
public class RoomMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomMateApplication.class, args);
    }

}
