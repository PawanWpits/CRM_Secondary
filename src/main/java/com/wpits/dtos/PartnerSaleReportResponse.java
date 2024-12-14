package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerSaleReportResponse {

	private int totalCustomerOnboard;
	
	private int prepaidCustomer;
	
	private int postpaidCustomer;
	
	private int deviceCustomer;
}
