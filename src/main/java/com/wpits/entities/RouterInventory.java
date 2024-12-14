package com.wpits.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RouterInventory {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private LocalDateTime creationTime; //LocalDateTime
	
	private String type;
	
	@Column(unique = true, nullable = false)
	private int serialNumber;   
	
	private String brand;
	
	private String deviceManufactorer;
	
	private int vendorId;
	
	private int ssId;
	
	private String cpeConfigUrl;
	
	private String cpeUsername;
	
	private String cpePassword;
	
	private LocalDateTime allocationDate;
	
	private LocalDateTime activationDate;
	
	@Enumerated(EnumType.STRING)
	private Status deviceStatus;
	
	private String deviceStaticIp;
	
	@Column(unique = true, nullable = false)
	private String macAddress;
	
	private int partnerId;
	
	private LocalDateTime passwordUpdationTime;
	
	@OneToMany(mappedBy = "routerInventory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Customer> customers;
	
	public enum Status{
		ACTIVE,INACTIVE;
	}

}
