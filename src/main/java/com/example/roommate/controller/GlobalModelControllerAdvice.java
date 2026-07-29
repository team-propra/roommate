package com.example.roommate.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalModelControllerAdvice {

    @ModelAttribute("languageUrlDe")
    public String languageUrlDe(HttpServletRequest request) {
        return languageUrl(request, "de");
    }

    @ModelAttribute("languageUrlEn")
    public String languageUrlEn(HttpServletRequest request) {
        return languageUrl(request, "en");
    }

    private static String languageUrl(HttpServletRequest request, String language) {
        List<String> parameters = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if ("lang".equals(entry.getKey())) {
                continue;
            }
            for (String value : entry.getValue()) {
                parameters.add(encode(entry.getKey()) + "=" + encode(value));
            }
        }
        parameters.add("lang=" + encode(language));
        return request.getRequestURI() + "?" + String.join("&", parameters);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
