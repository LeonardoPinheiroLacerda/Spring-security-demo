package com.leonardo.springsecuritydemo.services;

import javax.crypto.SecretKey;

import com.leonardo.springsecuritydemo.models.AppUser;
import com.leonardo.springsecuritydemo.security.jwt.JwtConfig;
import com.leonardo.springsecuritydemo.security.jwt.TokenGenerator;
import com.leonardo.springsecuritydemo.security.users.AppUserDetails;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@Service
public class AuthService {
    
    private final TokenGenerator tokenGenerator;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public AppUser getAuthenticated(){
        AppUserDetails details = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return details.getUser();
    }

    public String generateNewJtw(){
        AppUser user = getAuthenticated();
        return tokenGenerator.generate(user, jwtConfig, secretKey);
    }

}
