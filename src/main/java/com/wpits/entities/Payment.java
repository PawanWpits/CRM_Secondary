package com.wpits.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "cust_id")
	private int customerId;
	
	@Column(name = "partner_id")
	private int partnerId;
	
	@Column(name = "device_id")
	private int device;
	
	@Column(name = "msisdn")
	private String msisdn;
	
	@Column(name = "product")
	private String product;
	
	@Column(name = "plan_id")
	private int planId;
	
	@Column(name = "attempt")
	private int attempt;
	
	@Column(name = "amount", nullable = false)
	private Double amount;
	
	@Column(name = "create_datetime", nullable = false)
	private LocalDateTime createDatetime;
	
	@Column(name = "update_datetime")
	private LocalDateTime updateDatetime;
	
	@Column(name = "payment_date")
	private LocalDate paymentDate;
	
	@Column(name = "deleted", nullable = false)
	private int deleted;
	
	@Column(name = "is_refund", nullable = false)
	private int isRefund;
	
	@Column(name = "is_preauth", nullable = false)
	private int isPreauth;
	
	@Column(name = "payout_id")
	private int payoutId;
	
	@Column(name = "balance")
	private Double balance;
	
	@Column(name = "optlock")
	private int optlock;
	
	@Column(name = "payment_period")
	private int paymentPeriod;
	
	@Column(name = "payment_notes", length = 500)
	private String paymentNotes;
	
	@Column(name = "payment_status", nullable = false)
	private Boolean paymentStatus;
	
	@Column(name = "mode")
	private String mode;
	
	@Column(name = "transaction_id", unique = true)
	private String transactionId;
	
	@Column(name = "customer_type")
	private String customerType;
	
	@Column(name = "plan")
	private String plan;
	
	@Column(name = "invoice_id", nullable = false)
	private int custInvoiceId;
	
	@ManyToOne
	@JoinColumn(name = "currency_id", nullable = false)
	private Currency currency;
	
	@ManyToOne
	@JoinColumn(name = "credit_card_id")
	private CreditCard creditCard;
	
	@ManyToOne
	@JoinColumn(name = "result_id")
	private PaymentResult paymentResult;
	
	@ManyToOne
	@JoinColumn(name = "method_id")
	private PaymentMethod paymentMethod;
	

}
