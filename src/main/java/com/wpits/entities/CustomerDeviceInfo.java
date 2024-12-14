package com.wpits.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustomerDeviceInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int customerId;
	
//	LinkedList<Integer> deviceId = new LinkedList<Integer>();
	
	private int deviceId;
	
	private int orderId;
	
	private int partnerId;
	
	private int invoiceId;
	
	private int paymentId;
	
	private LocalDate purchaseDate;
	
	private String status;
	
}
