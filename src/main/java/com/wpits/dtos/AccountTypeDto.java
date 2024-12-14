package com.wpits.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTypeDto {

	private int id;
	
	private Double creditLimit;
	
	private String invoiceDesign;
	
	@JsonFormat(pattern = "yyyy-MMM-dd HH:mm:ss")
	private LocalDateTime dateCreated;
	
	private Double creditNotificationLimit1;
	private Double creditNotificationLimit2;
	private int optlock;
	private int nextInvoiceDayOfPeriod;
	private int notificationAitId;
	private CurrencyDto currency;
	
	private EntitysDto entitys;
	
	private LanguageDto language;
	
	private OrderPeriodDto orderPeriod;
	
	private InvoiceDeliveryMethodDto invoiceDeliveryMethod;
}
