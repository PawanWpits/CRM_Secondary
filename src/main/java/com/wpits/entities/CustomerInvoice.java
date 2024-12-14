package com.wpits.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer_invoice")
@Getter
@Setter
public class CustomerInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "details", columnDefinition = "TEXT", nullable = false)
    private String details;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;
    
    private String invoiceReferenceNo;
    
    private LocalDateTime invoiceDateTime;
}
