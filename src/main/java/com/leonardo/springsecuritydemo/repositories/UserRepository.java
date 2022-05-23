package com.leonardo.springsecuritydemo.repositories;

import com.leonardo.springsecuritydemo.models.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer>{

}
