package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PartnerDto;

public interface PartnerService {

	PartnerDto createPartner(PartnerDto partnerDto, int baseUserId, int partnerCommissionId);
	
	PartnerDto updatePartner(PartnerDto partnerDto, int partnerId);
	
	List<PartnerDto> getAllPartner();
	
	PartnerDto findByIdPartner(int partnerId);
	
	void deletePartner(int partnerId);
	
	PartnerDto findByPartnerContact(String contact);
}
