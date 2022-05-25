package com.leonardo.springsecuritydemo.security.jwt;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import com.leonardo.springsecuritydemo.models.AppUser;
import com.leonardo.springsecuritydemo.security.users.AppUserDetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class TokenGenerator {
    
    public String generate(Authentication authResult, JwtConfig jwtConfig, SecretKey secretKey){
        Set<GrantedAuthority> authorities = filterOnlyRoles(((AppUserDetails) authResult.getPrincipal()).getUser());        
        
        String token = Jwts.builder()
            .setSubject(authResult.getName())
            .claim("authorities", authorities)
            .setIssuedAt(Date.valueOf(LocalDate.now()))
            .setExpiration(Date.valueOf(LocalDate.now().plusDays(Integer.parseInt(jwtConfig.getTokenExpirationAfterDays()))))
            .signWith(secretKey)
            .compact();
        return JwtConfig.TOKEN_PREFIX + token;
    }

    public String generate(AppUser user, JwtConfig jwtConfig, SecretKey secretKey){
        Set<GrantedAuthority> authorities = filterOnlyRoles(user);
        
        String token = Jwts.builder()
            .setSubject(user.getUsername())
            .claim("authorities", authorities)
            .setIssuedAt(Date.valueOf(LocalDate.now()))
            .setExpiration(Date.valueOf(LocalDate.now().plusDays(Integer.parseInt(jwtConfig.getTokenExpirationAfterDays()))))
            .signWith(secretKey)
            .compact();
            
        return JwtConfig.TOKEN_PREFIX + token;
    }

    private Set<GrantedAuthority> filterOnlyRoles(AppUser user){
        return user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toSet());
    }

}
