package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInvoiceDto {

    private int invoiceId;
    
    private String orderNumber;
    
    private String customerName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    
    private double totalAmount;
    
    private List<InvoiceItemDTO> items;
    
    private String invoiceReferenceNo;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDateTime;
}
