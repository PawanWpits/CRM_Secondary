package com.wpits.services;

import java.util.List;
import java.util.Map;

import com.wpits.dtos.PartnerCommissionDto;

public interface PartnerCommissionService {

	PartnerCommissionDto createPartnerCommission(PartnerCommissionDto partnerCommissionDto, int currencyId);
	
	PartnerCommissionDto updatePartnerCommission(PartnerCommissionDto partnerCommissionDto, int partnerCommissionId);
	
	List<PartnerCommissionDto> getAllPartnerCommission();
	
	PartnerCommissionDto findByIdPartnerCommission(int partnerCommissionId);
	
	void deletePartnerCommision(int partnerCommissionId);
	
	
	List<Map<String, Object>> getCommissionByPartnerId(int partnerId);
}
