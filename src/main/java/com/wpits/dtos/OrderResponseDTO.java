package com.wpits.dtos;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {

    private int id;
    
    private String name;
    
    private String token;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    private int partnerId;
    
    private String orderNumber;
    
    private List<OrderItemDTO> orderDetails;
    
    private Double totalAmount;
}
