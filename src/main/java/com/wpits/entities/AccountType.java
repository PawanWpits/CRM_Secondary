package com.wpits.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_type")
@Getter
@Setter
public class AccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "credit_limit")
	private Double creditLimit;
	
	@Column(name = "invoice_design")
	private String invoiceDesign;
	
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	
	@Column(name = "credit_notification_limit1")
	private Double creditNotificationLimit1;
	
	@Column(name = "credit_notification_limit2")
	private Double creditNotificationLimit2;
	
	@Column(name = "optlock", nullable = false)
	private int optlock;
	
	@Column(name = "next_invoice_day_of_period", nullable = false)
	private int nextInvoiceDayOfPeriod;
	
	@Column(name = "notification_ait_id")
	private int notificationAitId;
	
	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;
	
	@ManyToOne
	@JoinColumn(name = "entity_id")
	private Entitys entitys;
	
	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;
	
	@ManyToOne
	@JoinColumn(name = "main_subscript_order_period_id",nullable = false)
	private OrderPeriod orderPeriod;
	
	@ManyToOne
	@JoinColumn(name = "invoice_delivery_method_id")
	private InvoiceDeliveryMethod invoiceDeliveryMethod;
	
	@OneToMany(mappedBy = "accountType",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Customer> customers;
	
	
}
