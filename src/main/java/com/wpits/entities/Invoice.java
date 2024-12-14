package com.wpits.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice")
@Getter
@Setter
public class Invoice {
	
	public enum Status{
		PAID, UNPAID;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "create_datetime",nullable = false)
	private LocalDateTime createDateTime;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "due_date")
	private LocalDate dueDate;
	
	@Column(name = "total",nullable = false)
	private Double total;
	
	@Column(name = "payment_attempts", nullable = false)
	private int paymentAttempts;
	
//	@Column(name = "status_id",nullable = false)
//	private int statusId;
	
	@Column(name = "customer_id", nullable = false)
	private int customerId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	
	@Column(name = "balance")
	private Double balance;
	
	@Column(name = "carried_balance",nullable = false)
	private Double carriedBalance;
	
	@Column(name = "in_process_payment",nullable = false)
	private int inProcessPayment;
	
	@Column(name = "is_review",nullable = false)
	private int isReview;
	
	@Column(name = "deleted",nullable = false)
	private int deleted;
	
	@Column(name = "customer_notes",length = 1000)
	private String customerNotes;
	
	@Column(name = "public_number",length = 40)
	private String publicNumber;
	
	private String msisdn;
	
	@Column(name = "last_reminder")
	private LocalDate lastReminder;
	
	@Column(name = "overdue_step")
	private int overdueStep;  //it's use for VIP
	
	@Column(name = "create_timestamp",nullable = false)
	private LocalDateTime createTimeStamp;
	
	@Column(unique = true, nullable = false)
	private String invoiceNumber;
	
	private LocalDate billStartDate;
	
	private LocalDate billEndDate;
	
	private Double billAmount;
	
	private Double lateFee;
	
	private boolean isVip;
	
	@Column(name = "optlock",nullable = false)
	private int optlock;
	
	@ManyToOne
	@JoinColumn(name = "billing_process_id")
	private BillingProcess billingProcess;
	
	@ManyToOne
	@JoinColumn(name = "paper_invoice_batch_id")
	private PaperInvoiceBatch paperInvoiceBatch;
	
	@ManyToOne
	@JoinColumn(name = "currency_id",nullable = false)
	private Currency currency;
	
	@ManyToOne
	@JoinColumn(name = "delegated_invoice_id")
	private Invoice invoice;
}
