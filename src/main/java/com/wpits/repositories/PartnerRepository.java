package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Integer>{

	Optional<Partner> findByContact(String contact);

}
