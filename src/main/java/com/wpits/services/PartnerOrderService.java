package com.wpits.services;

import java.util.Map;

import com.wpits.dtos.PartnerOrderRequest;

public interface PartnerOrderService {
	
	Map<String, Object> createPartnerOrder(PartnerOrderRequest prtOrderRequest);

}
