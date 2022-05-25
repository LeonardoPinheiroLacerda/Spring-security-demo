package com.leonardo.springsecuritydemo.resources;

import com.leonardo.springsecuritydemo.security.jwt.JwtConfig;
import com.leonardo.springsecuritydemo.services.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@RestController
@RequestMapping("/api/tests")
public class TestResource {
    
    private final AuthService authService;

    @GetMapping("/common")
    public String onlyCommon(){
        return "Only common";
    }

    @GetMapping("/admin")
    public String commonAndAdmin(){
        return "Common and admin";
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<Void> refreshToken(){
        String token = authService.generateNewJtw();
        return ResponseEntity
            .noContent()
            .header(JwtConfig.AUTHORIZATION_HEADER, token)
            .build();
    }

}
