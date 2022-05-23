package com.leonardo.springsecuritydemo.security.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor

@Slf4j

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final Environment environment;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       
        if(Arrays.asList(environment.getActiveProfiles()).contains("test")){
            log.warn("Perfil de teste ativo. Acesso ao console do H2 liberado!");

            http.csrf().disable();

            http.headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll();

        }

        return http.build();
    }

}
