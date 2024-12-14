package com.wpits.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.CustomerDeviceInfo;

public interface CustomerDeviceInfoRepository extends JpaRepository<CustomerDeviceInfo, Integer>{
	
	List<CustomerDeviceInfo> findByCustomerId(int customerId);
	
	Optional<CustomerDeviceInfo> findByDeviceId(int deviceId);
	

}
