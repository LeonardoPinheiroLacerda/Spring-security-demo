package com.leonardo.springsecuritydemo.security.configs;

import java.util.Arrays;

import com.leonardo.springsecuritydemo.models.enums.Role;
import com.leonardo.springsecuritydemo.security.users.AppUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor

@Slf4j

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final Environment environment;
    private final BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       
        //Libera acesso ao console do H2 via browser
        if(Arrays.asList(environment.getActiveProfiles()).contains("test")){
            log.warn("Perfil de teste ativo. Acesso ao console do H2 liberado!");

            http.csrf().disable();

            http.headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll();

        }

        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/tests/common").hasRole(Role.COMMON.name())
            .antMatchers(HttpMethod.GET, "/api/tests/admin").hasRole(Role.ADMIN.name())
            .anyRequest().authenticated()
            
            .and()
            //Impedindo a aplicação de criar sessões.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            //Defindo a estratégia de autenticação HTTP basic
            .httpBasic()
            /*
            Injetando o entry point da autenticação. No objeto basicAuthenticationEntryPoint contém um método que retorna um header que 
            informa os clientes que essa aplicação utiliza o HTTP Basic como protocólo de autenticação e autorização.
            */
            .authenticationEntryPoint(basicAuthenticationEntryPoint);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(5);
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, AppUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

}
