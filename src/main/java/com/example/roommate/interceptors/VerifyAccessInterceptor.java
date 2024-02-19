package com.example.roommate.interceptors;

import com.example.roommate.annotations.Interceptor;
import com.example.roommate.interfaces.application.services.IAuthenticationApplicationService;
import com.example.roommate.interfaces.entities.IUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Interceptor
public class VerifyAccessInterceptor implements HandlerInterceptor {

    // ...
    IAuthenticationApplicationService userApplicationService;

    @Autowired
    public VerifyAccessInterceptor(IAuthenticationApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (auth.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities1 = auth.getAuthorities();
            if(authorities1.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            else {
                authorities.addAll(authorities1);
            }
        }

        OAuth2User user = (OAuth2User) auth.getPrincipal();
        String login = user.getAttribute("login");

        IUser userFromDatabase = userApplicationService.getUserByLogin(login);

        if (userFromDatabase != null) {
            // add whatever authorities you want here
            String userRole = userFromDatabase.getRole();
            if (userRole.equals("ADMIN")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if (userRole.equals("VERIFIED_USER")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_VERIFIED_USER"));
            }
        }

        Authentication newAuth = null;

        if (auth.getClass() == OAuth2AuthenticationToken.class) {
            OAuth2User principal = ((OAuth2AuthenticationToken) auth).getPrincipal();
            if (principal != null) {
                newAuth = new OAuth2AuthenticationToken(principal, authorities, (((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId()));
            }
        }

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return true;
    }

    /*
    private String parsePrincipal(Object p) {
        String[] array = p.toString().split(",");

    }

     */

}
