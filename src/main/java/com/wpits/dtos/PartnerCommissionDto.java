package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerCommissionDto {

	private int id;
	
	private Double amount;
	
	private String type;
	
	private int partnerId;
	
	private int commissionProcessRunId;
	
	private CurrencyDto currency;
}
