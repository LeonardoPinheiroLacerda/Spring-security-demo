package com.leonardo.springsecuritydemo.services;

import java.util.Arrays;

import com.google.common.collect.Sets;
import com.leonardo.springsecuritydemo.models.AppUser;
import com.leonardo.springsecuritydemo.models.enums.Role;
import com.leonardo.springsecuritydemo.repositories.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@Service
public class DbService implements CommandLineRunner{
    
    private final UserRepository repository;

    public void mock(){

        AppUser user1 = new AppUser(null, "Leonardo", "Lacerda", "leon", "senha123", Sets.newHashSet(Role.ADMIN, Role.COMMON));
        AppUser user2 = new AppUser(null, "Roberio", "Silva", "rob", "senha123", Sets.newHashSet(Role.COMMON));

        repository.saveAll(Arrays.asList(user1, user2));

    }

    @Override
    public void run(String... args) throws Exception {
        mock();
    }

}
