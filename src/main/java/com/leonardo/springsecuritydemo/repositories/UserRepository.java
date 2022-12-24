package com.leonardo.springsecuritydemo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leonardo.springsecuritydemo.models.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer>{

    @Query("SELECT user FROM AppUser user WHERE user.username = ?1")
    public Optional<AppUser> findByUsername(String username);

}
