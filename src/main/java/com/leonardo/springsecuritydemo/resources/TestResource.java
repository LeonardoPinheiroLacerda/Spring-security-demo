package com.leonardo.springsecuritydemo.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tests")
public class TestResource {
    
    @GetMapping("/common")
    public String onlyCommon(){
        return "Only common";
    }

    @GetMapping("/admin")
    public String commonAndAdmin(){
        return "Common and admin";
    }

}
