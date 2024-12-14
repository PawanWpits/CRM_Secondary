package com.wpits.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PartnerInvoice {
	
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @OneToOne
	    @JoinColumn(name = "partner_order_id", nullable = false)
	    private PartnerOrder partnerOrders;
	    
	    @Column(name = "amount")
	    private double amount;
	    
	    @Column(name = "offered_discount")
	    private int offeredDiscount;

	    @Column(name = "pay_amount", nullable = false)
	    private double payAmount;

	    @Column(name = "invoice_date", nullable = false)
	    private LocalDateTime invoiceDate;

	    @Column(name = "due_date", nullable = false)
	    private LocalDateTime dueDate;
	    
	    @Column(name = "payment_status")
	    private boolean paymentStatus;
	    
	    @Column(name = "invoice_number", unique = true,nullable = false)
	    private String invoiceNumber;
	    
	    @Column(name = "product", nullable = false)
	    private String product;

	    @Column(name = "product_type", nullable = false)
	    private String productType;
	    
	    @Column(name = "partner_id", nullable = false)
	    private int partnerId;
	    
	    private int totalUnits;


}
