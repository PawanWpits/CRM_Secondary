package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpits.entities.BillingProcess;
import com.wpits.entities.Currency;
import com.wpits.entities.Invoice;
import com.wpits.entities.Invoice.Status;
import com.wpits.entities.PaperInvoiceBatch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDto {

	private int id;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDateTime;
	
	private int userId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dueDate;
	private Double total;
	private int paymentAttempts;
//	private int statusId;
	private int customerId;
	private Status status;
	private Double balance;
	private Double carriedBalance;
	private int inProcessPayment;
	private int isReview;
	private int deleted;
	private String customerNotes;
	private String publicNumber;
	private String msisdn;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate lastReminder;
	private int overdueStep;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTimeStamp;
	private String invoiceNumber;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate billStartDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate billEndDate;
	private Double billAmount;
	private Double lateFee;
	private boolean isVip;
	private int optlock;
	private BillingProcessDto billingProcess;
	private PaperInvoiceBatchDto paperInvoiceBatch;
	private CurrencyDto currency;
	private InvoiceDto invoice;
}
