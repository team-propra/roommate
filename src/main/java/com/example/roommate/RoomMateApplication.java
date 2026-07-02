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
        SpringApplication application = new SpringApplication(RoomMateApplication.class);
        application.addInitializers(new LocalInfrastructureInitializer());
        application.run(args);
    }

}
