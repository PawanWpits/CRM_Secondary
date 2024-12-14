package com.wpits.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.CustomerDto;
import com.wpits.dtos.RouterInventoryDto;

public interface RouterInventoryService {
	
	RouterInventoryDto createRouterInventory(RouterInventoryDto routerInventoryDto);
	
	void saveExcel(MultipartFile file, int vendorId) throws IOException; 
	
	List<RouterInventoryDto> getAllRouters();
	
	List<Map<String, Object>> partnerAssignedRouters(int partnerId);
	
	Map<String, Object> changePassword(String macAddress,String password);
	
	CustomerDto getCustomerDetailsOfRouter(String macAddress);

}
