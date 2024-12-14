package com.wpits.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.PartnerCommission;

public interface PartnerCommissionRepository extends JpaRepository<PartnerCommission, Integer>{
	
	Optional<List<PartnerCommission>> findByPartnerId(int partnerId);

}
