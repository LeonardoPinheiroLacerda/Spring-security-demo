package com.leonardo.springsecuritydemo.security.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

@Configuration
@ConfigurationProperties(prefix = "application.formlogin.rememberme")
public class RememberMeProperties {
    
    private String key;
    private String cookie;
    private String validityDays;

    public Integer getValidityDays(){
        return Integer.parseInt(this.validityDays);
    }

}
