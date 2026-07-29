package com.example.roommate.xcepto.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@TestConfiguration
public class ControllerRouteSecurityConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain fullHttpTestSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(HttpMethod.GET, "/", "/rooms", "/room/*/workspace/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/access").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .addFilterAfter(new OAuth2ShapeForBasicUsersFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{noop}admin").roles("ADMIN").build(),
                User.withUsername("verified").password("{noop}verified").roles("VERIFIED_USER").build()
        );
    }

    static class OAuth2ShapeForBasicUsersFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                    && authentication.isAuthenticated()
                    && !(authentication instanceof OAuth2AuthenticationToken)) {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                String login = authentication.getName();
                var principal = new DefaultOAuth2User(
                        authorities,
                        Map.of("id", login.hashCode(), "login", login),
                        "id"
                );
                SecurityContextHolder.getContext().setAuthentication(
                        new OAuth2AuthenticationToken(principal, authorities, "github")
                );
            }

            filterChain.doFilter(request, response);
        }
    }
}
