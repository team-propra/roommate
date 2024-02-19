package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.interfaces.application.services.IAuthenticationApplicationService;
import com.example.roommate.interfaces.entities.IUser;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@ApplicationService
@SuppressFBWarnings(value="EI2", justification="UserDomainService is properly injected")
public class AuthenticationApplicationService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>, IAuthenticationApplicationService {
    UserDomainService userDomainService;

    public AuthenticationApplicationService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User originalUser = defaultService.loadUser(userRequest);
        Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());

        String login = originalUser.getAttribute("login");
        String userRole;
        IUser userByLogin = getUserByLogin(login);
        if (userByLogin != null) {
            userRole = userByLogin.getRole();
            if (userRole.equals("ADMIN")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if (userRole.equals("VERIFIED_USER")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_VERIFIED_USER"));
            }
        }
        return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "id");
    }


    
    public IUser getUserByLogin(String login) {
        return userDomainService.getUserByLogin(login);
    }

    public boolean isVerified(String login) {
        if(getUserByLogin(login) == null) {
            userDomainService.addUser(login);
            return false;
        }
        else {
            return getUserByLogin(login).getRole().equals("VERIFIED_USER");
        }
    }

    public void registerKey(UUID keyId, String login) {
        userDomainService.registerKey(keyId, login);
    }

    public boolean userHasKey(String login) {
        if(userDomainService.getUserByLogin(login) == null) {
            userDomainService.addUser(login);
            return false;
        }
        IUser user = userDomainService.getUserByLogin(login);
        return user.getKeyId() != null;
    }
}
