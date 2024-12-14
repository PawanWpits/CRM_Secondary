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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "partner_payment")
@Getter
@Setter
public class PartnerPayment {
	
	public enum Status{
		PAID,UNPAID;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "partner_id", nullable = false)
	private int partnerId;
	
	@Column(name = "product", nullable = false)
	private String product;
	
	@Column(name = "product_type", nullable = false)
	private String productType;
	
	@Column(name = "total_units", nullable = false)
	private int totalUnits;
	
	@Column(name = "amount", nullable = false)
	private Double amount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime paymentDateTime;
	
	private int currency;
	
	private int creditCard;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "payment_ref_id", unique = true,nullable = false)
	private String paymentRefId;
	
    @Column(name = "invoice_number", unique = true,nullable = false)
    private String invoiceNumber;
    
    private String paymentMode;
}
