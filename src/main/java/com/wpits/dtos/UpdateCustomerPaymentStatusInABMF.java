package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerPaymentStatusInABMF {

	private int customer_id;
	
	private Boolean payment_status;
}
