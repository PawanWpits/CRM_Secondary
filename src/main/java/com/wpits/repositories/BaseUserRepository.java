package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.BaseUser;

public interface BaseUserRepository extends JpaRepository<BaseUser, Integer>{

	Optional<BaseUser> findByEmail(String email);
}
