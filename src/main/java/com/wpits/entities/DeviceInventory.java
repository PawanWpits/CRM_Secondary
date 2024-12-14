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
@Table(name = "device_inventory")
@Getter
@Setter
public class DeviceInventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "device_model")
	private String deviceModel;
	
	@Column(name = "imi", unique = true, nullable = false)
	private String imi;
	
	@Column(name = "vendor_id")
	private Integer vendorId;
	
	@Column(name = "device_make")
	private String deviceMake;
	
	@Column(name = "configuration")
	private String configuration;
	
	@Column(name = "OSType")
	private String ostype;
	
	@Column(name = "manufacturer")
	private String manufacturer;
	
	@Column(name = "manufacture_date")
	private LocalDate manufactureDate;
	
    @Column(name = "batch_id")
    private Integer batchId;   
    
    @Column(name = "device_type")
    private String deviceType;
     
//    @Column(name = "vendor_name")
//    private String vendorName;
//    
//    @Column(name = "vendor_contact")
//    private String vendorContact;
//    
//    @Column(name = "vendor_address")
//    private String vendorAddress;
    
    @Column(name = "buying_price_usd")
    private Double buyingPriceUsd;
    
    @Column(name = "selling_price_usd")
    private Double sellingPriceUsd;
    
    @Column(name = "vat")
    private String vat;
    
    @Column(name = "other_taxes")
    private Double otherTaxes;
    
    @Column(name = "min_commision")
    private Double minCommision;
    
    @Column(name = "max_commision")
    private Double maxCommision;
    
    @Column(name = "avg_commision")
    private Double avgCommision;
    
    @Column(name = "partner_id")
    private int partnerId;
    
    private LocalDate allocationDate;
    
    @OneToMany(mappedBy = "deviceInventory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Customer> customers;
}
