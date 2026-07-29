package com.example.roommate.interceptors;

import com.example.roommate.annotations.Interceptor;
import com.example.roommate.interfaces.application.services.IAuthenticationApplicationService;
import com.example.roommate.interfaces.entities.IUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Interceptor
public class VerifyAccessInterceptor extends OncePerRequestFilter {
    private static final Set<String> DATABASE_MANAGED_AUTHORITIES = Set.of(
            "ROLE_USER", "ROLE_VERIFIED_USER", "ROLE_ADMIN", "ROLE_INJECTED_ADMIN");

    private final IAuthenticationApplicationService authenticationApplicationService;

    public VerifyAccessInterceptor(IAuthenticationApplicationService authenticationApplicationService) {
        this.authenticationApplicationService = authenticationApplicationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken token) {
            OAuth2User principal = token.getPrincipal();
            String login = principal.getAttribute("login");
            if (login != null) {
                Set<GrantedAuthority> authorities = authentication.getAuthorities().stream()
                        .filter(authority -> !DATABASE_MANAGED_AUTHORITIES.contains(authority.getAuthority()))
                        .collect(Collectors.toSet());

                IUser user = authenticationApplicationService.getUserByLogin(login);
                if (user != null) {
                    user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }

                SecurityContextHolder.getContext().setAuthentication(
                        new OAuth2AuthenticationToken(principal, authorities, token.getAuthorizedClientRegistrationId()));
            }
        }

        filterChain.doFilter(request, response);
    }
}
