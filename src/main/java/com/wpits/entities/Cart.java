package com.wpits.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String token;
	
	/*	@Column(unique = true, nullable = false)*/
	private int deviceId; 
	
	private int routerSerialNo;
	
	private Double amount;
	
	private int partnerId;
	
	private String type;
	
	private String name;
	
	private LocalDate createDate;
	
	private LocalDate expiryDate;
}
