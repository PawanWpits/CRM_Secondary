package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerSimResponse {
	
	private String msisdn;
	
	private String imsi;
	
	private String category;
	
	private String simType;
	
	private Double amount;
	
	
}
