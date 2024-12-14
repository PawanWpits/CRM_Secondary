package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    private String name;

    private int quantity;

    private double price;
    
    private String productType;
    
    private String number;
    
    private int productSerialNo;
}
