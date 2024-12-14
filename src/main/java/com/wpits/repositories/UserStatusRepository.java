package com.wpits.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Integer>{

}
