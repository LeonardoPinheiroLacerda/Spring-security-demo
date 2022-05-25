package com.leonardo.springsecuritydemo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CredentialsDTO {
    
    private String username;
    private String password;

}
