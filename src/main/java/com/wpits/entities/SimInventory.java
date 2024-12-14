package com.wpits.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sim_inventory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SimInventory implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name ="msisdn", unique = true, nullable = false)
	private String msisdn;   //unique msisdn lega
	
	@Column(name = "category")
	private String category; //(M2M,Normal)
	
	@Column(name = "special_number")
	private Boolean specialNumber;
	 
    @Column(name = "imsi", unique = true, nullable = false)
    private String imsi;
 
    @Column(name = "p_imsi")
    private String pimsi;
 
    @Column(name = "batch_id")
    private Integer batchId;
    
	@Column(name = "batch_date")
	private LocalDate batchDate;
 
    @Column(name = "vendor_id")
    private Integer vendorId;
 
    @Column(name = "status")
    private Boolean status;
 
    @Column(name = "prov_status")
    private Boolean provStatus;
 
    @Column(name = "allocation_date")
    private LocalDateTime allocationDate;
 
    @Column(name = "activation_date")
    private LocalDateTime activationDate;
    
//    @Column(name = "vendor_name")
//    private String vendorName;
//    
//    @Column(name = "vendor_contact")
//    private String vendorContact;
//    
//    @Column(name = "vendor_address")
//    private String vendorAddress;
    
    @Column(name = "sim_type")
    private String simType;
    
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
    
    @Column(name = "validity_days")
    private int validityDays;
    
    @Column(name = "activation_status")
    private String activationStatus;
    
    @Column(name = "activation_code",unique = true, nullable = false )
    private String activationCode;
    
    @Column(name = "activation_token", unique = true, nullable = false)
    private String activationToken;
}
