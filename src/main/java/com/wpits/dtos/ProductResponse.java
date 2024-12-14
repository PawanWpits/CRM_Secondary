package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	
	private int deviceId;
	
	private String deviceModel;
	
	private String deviceType;
	
	private String osType;
	
	private double price;

}
