package com.leonardo.springsecuritydemo.security.configs;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor

@Slf4j

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final Environment environment;
    private final RememberMeProperties rememberMeProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       
        //Libera acesso ao console do H2 via browser
        if(Arrays.asList(environment.getActiveProfiles()).contains("test")){
            log.warn("Perfil de teste ativo. Acesso ao console do H2 liberado!");

            http.csrf().disable();

            http.headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/h2-console/**").permitAll();

        }

        http.authorizeHttpRequests(authorization -> {
            authorization
                .requestMatchers(HttpMethod.GET, "/api/tests/common").hasRole(Role.COMMON.name())
                .requestMatchers(HttpMethod.GET, "/api/tests/admin").hasRole(Role.ADMIN.name())
                .anyRequest().authenticated();
        });
            
            
        http
            //Define a estratégia de autenticação e autorização como 'formLogin'
            .formLogin()
            
            //Define a página de login e o nome dos campos que a aplicação vai esperar para autenticação
            .loginPage("/login").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
            
            .and()
            //Define a opção de remember-me e configura seus paramêtros.
            .rememberMe()
                .rememberMeParameter("rememberme")
                .rememberMeCookieName(rememberMeProperties.getCookie())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(rememberMeProperties.getValidityDays()))
                .key(rememberMeProperties.getKey())
        
            .and()
            //Define o end-point de logout e seu comportamento
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.POST.name()))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", rememberMeProperties.getCookie())
                .logoutSuccessUrl("/login");

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
