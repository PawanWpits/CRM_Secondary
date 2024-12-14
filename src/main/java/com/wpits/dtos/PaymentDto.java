package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {

	private int id;
	
	private int userId;
	
	private int partnerId;
	
	private int deviceId;
	
	private int customerId;
	
	private String msisdn;
	
	private int attempt;
	
	private Double amount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDatetime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDatetime;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate paymentDate;
	
	private int deleted;
	
	private int isRefund;
	
	private int isPreauth;
	
	private int payoutId;
	
	private int custInvoiceId;
	
	private Double balance;
	
	private int optlock;
	
	private int paymentPeriod;
	
	private String product;
	
	private int planId;
	
	private String paymentNotes;
	
	private Boolean paymentStatus;
	
	private String mode;
	
	private String customerType;
	
	private String plan;
	
	private String transactionId;
	
	private CurrencyDto currency;
	
	private PaymentResultDto paymentResult;
	
	private PaymentMethodDto paymentMethod;
	
	private CreditCardDto creditCard;
		
}
