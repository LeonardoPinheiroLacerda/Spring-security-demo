package com.leonardo.springsecuritydemo.security.jwt;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
    
    private String secretKey;
    private String tokenExpirationAfterDays;
    public final static Integer TIMEZONE_OFFSET = 0;
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";

    @Bean
    public SecretKey secretKeyForSignin(){
        return Keys.hmacShaKeyFor(getSecretKey().getBytes());
    }

}
