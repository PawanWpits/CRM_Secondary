package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.CustomerInfo;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Integer>{

	Optional<CustomerInfo> findByCustomerId(int id);

}
