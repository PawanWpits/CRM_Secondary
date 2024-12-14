package com.wpits.services;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.PartnerSimResponse;
import com.wpits.dtos.SimInventoryDto;

public interface SimInventoryService {

	SimInventoryDto createSimInventory(SimInventoryDto simInventoryDto);
	
	SimInventoryDto updateSimInventory(SimInventoryDto simInventoryDto, int simInventoryId);
	
	List<SimInventoryDto> getAllSimInventory();
	
	SimInventoryDto getByIdSimInventory(int simInventoryId);
	
	void deleteSimInventory(int simInventoryId);
	
	SimInventoryDto findByMsisdn(String msisdn);
	
	//excel
	void saveExcel(MultipartFile file, int vendorId);
	
	List<PartnerSimResponse> PartnerAssignedSim(int partnerId);
	
	Map<String, Map<String, Long>> allInventoryProductsCountOfPartner(int partnerId);
	
	Map<String, String> activationToken(String msisdn, String activationCode);
	
	Map<String, String> simActivationAndDeactivation(String activationToken, String simService);
	
	ApiResponseMessage simActivationBySms(int shortCode,String text, String msisdn);
	
	Map<String, String> getOtpByMsisdn(String msisdn);
	
	ApiResponseMessage simActivationByWeb(String otp, String msisdn);
}
