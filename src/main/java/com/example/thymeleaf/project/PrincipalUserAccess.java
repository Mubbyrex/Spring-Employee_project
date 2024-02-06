package com.example.thymeleaf.project;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Service
public class PrincipalUserAccess {
    private Authentication authentication;

    public Map<String,String> userInfoCollector(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse(null);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", currentUserName);
            userInfo.put("role", role);
            return userInfo;
        } else return Collections.emptyMap() ;
    };

}
