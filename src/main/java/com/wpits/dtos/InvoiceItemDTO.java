package com.wpits.dtos;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
    
    private String productType;
    
    private String Number;
    
    private int productSerialNo;
    
    private int quantity;
    
    private double price;
    
    private double taxRate;
    
    private double taxAmount;
    
    private double priceWithTax;
}
