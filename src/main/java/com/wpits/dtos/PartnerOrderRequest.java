package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerOrderRequest {

	private Integer partnerId;

	private String product;

	private String startingNumber;

	private String endingNumber;

	private String productType;

	private Integer totalUnits;
	
	private int offeredDiscount;

}
