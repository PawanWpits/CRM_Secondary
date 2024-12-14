package com.wpits.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wpits.entities.RouterInventory;

public interface RouterInventoryRepository extends JpaRepository<RouterInventory, Integer>{

	Optional<RouterInventory> findBySerialNumber(int routerSerialNo);
	
	List<RouterInventory> findByActivationDateIsNullAndAllocationDateIsNull();
	
	@Query(value = "SELECT * FROM wpbilling.router_inventory WHERE activation_date IS NULL And allocation_date IS NULL AND type = ?1 limit ?2", nativeQuery = true)
	List<RouterInventory> findAvailableDevices(String productType, Integer totalUnits);

	List<RouterInventory> findByPartnerIdAndActivationDateIsNullAndAllocationDateIsNull(int partnerId);

	List<RouterInventory> findByPartnerId(int partnerId);

	Optional<RouterInventory> findByMacAddress(String macAddress);

}
