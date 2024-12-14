package com.wpits.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer_info")
@Getter
@Setter
public class CustomerInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "cust_id",nullable = false)
	private int customerId;
	
	@Column(name = "msisdn_inventory_id",nullable = false)
	private int msisdnInventoryId;
	
	@Column(name = "payment_id",nullable = false)
	private int paymentId;
}
