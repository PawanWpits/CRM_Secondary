package com.wpits.entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PartnerOrder {

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
	    
	    @Column(name = "total_amount", nullable = false)
	    private double totalAmount;
	    
	    @Column(name = "payment_status")
	    private boolean paymentStatus;

	    @Column(name = "order_date", nullable = false)
	    private LocalDateTime orderDate;
	    
	    @Column(name = "order_number", unique = true,nullable = false)
	    private String orderNumber;
}
