package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerProductMappingDto {

	private int id;
	
	private int productId;
	
	private String commissionType;
	
	private String commissionValue;
	
	private PartnerDto partner;
}
