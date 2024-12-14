package com.wpits.repositories;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.wpits.entities.SimInventory;

public interface SimInventoryRepository extends JpaRepository<SimInventory, Integer>{
	
	Optional<SimInventory> findByMsisdn(String msisdn);
	
	@Transactional
	@Modifying
	@Query(value="update sim_inventory set allocation_date=?1 where msisdn=?2 ;",nativeQuery = true)
	void updateAllocationDate(LocalDateTime allocationDate, String msisdn);

	
	@Transactional
	@Modifying
	@Query(value="update sim_inventory set activation_date=?1 where msisdn=?2 ;",nativeQuery = true)
	void updateActivationDate(LocalDateTime activationDate, String msisdn );
	
	
	//List<SimInventory> findBySimTypeAndAllocationDateIsNullAndActivationDateIsNullAndPartnerIdIsNull(String simType);
	
	@Query(value = "SELECT * FROM wpbilling.sim_inventory WHERE activation_date IS NULL AND allocation_date IS NULL AND partner_id = 0 AND sim_type = ?1", nativeQuery = true)
	List<SimInventory> findAvailableSims(String simType);
	
	@Query(value = "SELECT * FROM wpbilling.sim_inventory WHERE activation_date IS NULL AND allocation_date IS NULL AND partner_id = ?1", nativeQuery = true)
	List<SimInventory> findByPartnerId(int partnerId);
	
	@Query(value = "SELECT * FROM wpbilling.sim_inventory WHERE partner_id = ?1", nativeQuery = true)
	List<SimInventory> findByPartnerIdTotalSim(int partnerId);
	
	@Query(value = "SELECT * FROM wpbilling.sim_inventory WHERE allocation_date IS NOT NULL AND partner_id = ?1", nativeQuery = true)
	List<SimInventory> findByPartnerIdAllocationSim(int partnerId);
	

	@Query(value = "SELECT * FROM wpbilling.sim_inventory WHERE activation_date IS NOT NULL AND partner_id = ?1", nativeQuery = true)
	List<SimInventory> findByPartnerIdActivationSim(int partnerId);

	Optional<SimInventory> findByActivationToken(String activationToken);
	


    @Query(value = "SELECT * FROM sim_inventory WHERE msisdn IN (:msisdns) AND activation_date IS NULL AND allocation_date IS NULL AND partner_id = 0 AND sim_type = :simType", nativeQuery = true)
    List<SimInventory> findAvailableSimDetails(@Param("msisdns") List<String> msisdns, @Param("simType") String simType);

    
	//Optional<SimInventory> findByActivationToken(String activationToken);

	//Optional<SimInventory> findByActivationCode(String activationCode);
}
