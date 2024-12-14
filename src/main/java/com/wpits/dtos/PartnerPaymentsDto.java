package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpits.entities.PartnerPayment.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerPaymentsDto {
	
	private int id;
	
	private int partnerId;
	
	private String product;
	
	private String productType;
	
	private int totalUnits;
	
	private Double amount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime paymentDateTime;
	
	private int currency;
	
	private int creditCard;
	
	private Status status;
	
	private String paymentRefId;
	
	private String invoiceNumber;
	
	private String paymentMode;

}
