package com.example.roommate.xcepto.controller;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;
import java.time.Duration;

public final class RoommateHttpClients {
    private RoommateHttpClients() {
    }

    public static HttpClient anonymousBrowser() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static HttpClient browserFor(String username, String password) {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                })
                .build();
    }
}
