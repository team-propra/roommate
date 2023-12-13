package com.example.roommate.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
        chainBuilder.authorizeHttpRequests(
                        configurer -> configurer
                                .requestMatchers("/", "/login").permitAll()
                                .anyRequest().authenticated()
                ) .formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .permitAll()
        );

        return chainBuilder.build();
    }

}
