package com.wpits.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.DeviceInventoryDto;
import com.wpits.dtos.ProductResponse;

public interface DeviceInventoryService {
	
	DeviceInventoryDto createDeviceInventory(DeviceInventoryDto deviceInventoryDto);
	
	DeviceInventoryDto updateDeviceInventory(DeviceInventoryDto deviceInventoryDto, int deviceInventoryId);
	
	List<DeviceInventoryDto>getAllDeviceInventory();
	
	DeviceInventoryDto getByIdDeviceInventory(int deviceInventoryId);
	
	void deleteDeviceInventory(int deviceInventoryId);
	
	//excel
	void saveExcel(MultipartFile file, int vendorId);

	List<ProductResponse> PartnerAssignedDevice(int partnerId);

	List<ProductResponse> SelfCareDevices();
	
//	Map<String, Integer> totalDeviceInfoPartner(int partnerId);
	
	void saveDeviceInfo(DeviceInventoryDto deviceInventoryDto,MultipartFile image) throws IOException;
}
