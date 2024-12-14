package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceInventoryDto {

	private int id;
	private String deviceModel;
	private String imi;
	private Integer vendorId;
	private String deviceMake;
	private String configuration;
	private String ostype;
	private String manufacturer;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate manufactureDate;
	private Integer batchId;
	private String deviceType;
//	private String vendorName;
//	private String vendorContact;
//	private String vendorAddress;
	private Double buyingPriceUsd;
	private Double sellingPriceUsd;
	private String vat;
	private Double otherTaxes;
	private Double minCommision;
	private Double maxCommision;
	private Double avgCommision;
	private int partnerId;
	private LocalDate allocationDate;
}
