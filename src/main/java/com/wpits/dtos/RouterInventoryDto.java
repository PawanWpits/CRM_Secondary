package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpits.entities.RouterInventory.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouterInventoryDto {
	
	private String id;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime creationTime;
	
	private String type;
	
	private int serialNumber;
	
	private String brand;
	
	private String deviceManufactorer;
	
	private int vendorId;
	
	private int ssId;
	
	private String cpeConfigUrl;
	
	private String cpeUsername;
	
	private String cpePassword;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime allocationDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime activationDate;
	
	private Status deviceStatus;
	
	private String deviceStaticIp;
	
	private String macAddress;

	private int partnerId;
	
	private LocalDateTime passwordUpdationTime;
}
