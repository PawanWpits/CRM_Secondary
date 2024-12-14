package com.wpits.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wpits.entities.DeviceInventory;

public interface DeviceInventoryRepository extends JpaRepository<DeviceInventory, Integer>{

	@Query(value = "SELECT * FROM wpbilling.device_inventory WHERE allocation_date IS NULL AND partner_id = 0 AND device_model = ?1 limit ?2", nativeQuery = true)
	List<DeviceInventory> findAvailableDevices(String deviceModel, int limit);
	
	@Query(value = "SELECT * FROM wpbilling.device_inventory WHERE allocation_date IS NULL AND partner_id = ?1", nativeQuery = true)
	List<DeviceInventory> findByPartnerId(int partnerId);
	
	@Query(value = "SELECT * FROM wpbilling.device_inventory WHERE partner_id = ?1", nativeQuery = true)
	List<DeviceInventory> findByPartnerAllDevice(int partnerId);
}
