package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDto {

	private int id;

	private String symbol;

	private String code;

	private String countryCode;

	private int optlock;
	
	private String description;
	
	private Double rate;
	
	private Double sysRate;
	
	private Boolean inUse;
}
