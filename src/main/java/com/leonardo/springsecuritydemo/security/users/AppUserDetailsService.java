package com.leonardo.springsecuritydemo.security.users;

import com.leonardo.springsecuritydemo.repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@Service
public class AppUserDetailsService implements UserDetailsService{

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserDetails(
            repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Não foi encontrado um usuário com esse username"))
        );
    }
    
}
