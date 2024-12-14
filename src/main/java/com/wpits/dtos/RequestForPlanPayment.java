package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestForPlanPayment {

	private String msisdn;
	
	private Double Amount;
	
	private String product;
	
	private int planId;
	
	private boolean paymentStatus;
}
