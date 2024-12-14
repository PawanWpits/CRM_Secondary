package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SimInventoryDto {

	private int id;
	
	@NotBlank(message = "msisdn not blank, pls drop the msisdn !!")
	@NotNull
	@Size(min = 11, max = 11, message = "msisdn should be 11 digit !!")
	private String msisdn;
	
	private String category; //(M2M,Normal)
	
	private Boolean specialNumber;

    private String imsi;

    private String pimsi;

    private Integer batchId;

    @JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate batchDate;

    private Integer vendorId;

    private Boolean status;

    private Boolean provStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime allocationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activationDate;

//    private String vendorName;
//
//    private String vendorContact;
//
//    private String vendorAddress;

    private String simType;
 
    private Double buyingPriceUsd;
   
    private Double sellingPriceUsd;

    private String vat;

    private Double otherTaxes;

    private Double minCommision;

    private Double maxCommision;

    private Double avgCommision;
    
    private int partnerId;
    
    private int validityDays;
    
    private String ActivationStatus;
    
    private String ActivationCode;
    
    private String ActivationToken;
	
}
