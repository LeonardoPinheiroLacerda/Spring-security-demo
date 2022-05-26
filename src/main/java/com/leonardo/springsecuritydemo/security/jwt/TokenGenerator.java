package com.leonardo.springsecuritydemo.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
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
            .setIssuedAt(getIssuedAt())
            .setExpiration(getExpiration(jwtConfig))
            .signWith(secretKey)
            .compact();
        return JwtConfig.TOKEN_PREFIX + token;
    }

    public String generate(AppUser user, JwtConfig jwtConfig, SecretKey secretKey){
        Set<GrantedAuthority> authorities = filterOnlyRoles(user);
        
        String token = Jwts.builder()
            .setSubject(user.getUsername())
            .claim("authorities", authorities)
            .setIssuedAt(getIssuedAt())
            .setExpiration(getExpiration(jwtConfig))
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

    private Date getIssuedAt(){
        ZoneOffset zoneOffset = ZoneOffset.ofHours(JwtConfig.TIMEZONE_OFFSET);
        Instant instant = LocalDateTime
            .now()
            .toInstant(zoneOffset);

        return Date.from(instant);
    }

    private Date getExpiration(JwtConfig jwtConfig){
        ZoneOffset zoneOffset = ZoneOffset.ofHours(JwtConfig.TIMEZONE_OFFSET);
        Instant instant = LocalDateTime
            .now()
            .plusDays(Long.valueOf(jwtConfig.getTokenExpirationAfterDays()))
            .toInstant(zoneOffset);

        return Date.from(instant);
    }

}
