package com.wpits.services;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.EkycCrmMappingDto;

public interface EkycCrmMappingService {

	EkycCrmMappingDto createCustomerFromEkyc(EkycCrmMappingDto ekycCrmMappingDto);
	
	ApiResponseMessage createCustomerFromKycForDevice(String token,int deviceId, int partnerId);
	
	ApiResponseMessage createCustomerFromKycForSim(String token,String msisdn, int partnerId, String customerType, String serviceType);
}
