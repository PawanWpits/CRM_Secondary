package com.wpits.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "credit_card")
@Getter
@Setter
public class CreditCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "cc_number",nullable = false,length = 100)
	private String ccNumber;
	
	@Column(name = "cc_number_plain",length = 20)
	private String ccNumberPlain;
	
	@Column(name = "cc_expiry",nullable = false)
	private LocalDate ccExpiry;
	
	@Column(name = "name",length = 150)
	private String name;
	
	@Column(name = "cc_type",nullable = false)
	private int ccType;
	
	@Column(name = "deleted",nullable = false)
	private int deleted =0;
	
	@Column(name = "gateway_key",length = 100)
	private String gatewayKey;
	
	@Column(name = "optlock",nullable = false)
	private int optlock;
	
	@OneToMany(mappedBy = "creditCard" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Payment> payments;
}
