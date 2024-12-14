package com.wpits.entities;

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
@Table(name = "currency")
@Getter
@Setter
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "symbol",nullable = false)
	private String symbol;
	
	@Column(name = "code",nullable = false)
	private String code;
	
	@Column(name = "country_code",nullable = false)
	private String countryCode;
	
	@Column(name = "optlock")
	private int optlock;
//field increase by ajay sir & krishna	
	@Column(name = "description")
	private String description;
	
	@Column(name = "rate", nullable = false)
	private Double rate;
	
	@Column(name = "sys_rate", nullable = false)
	private Double sysRate;
	
	@Column(name = "in_use")
	private Boolean inUse;
	
	@OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Entitys> entitys;
	
	@OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AccountType> accountTypes;
	
	@OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<BaseUser> baseUsers;
	
	@OneToMany(mappedBy = "currency",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Invoice> invoices;
	
	@OneToMany(mappedBy = "currency", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<PartnerCommission> partnerCommissions;
}
