package com.wpits.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
	
	private String name;
	
//    private int prepaidSim;
//    
//    private int postpaidSim;
//    
//    private int device;
    
//   @Column(unique = true)
	private String token;
	
	private LocalDate date;
	
	private int partnerId;
	
	@Column(unique = true)
	private String orderNumber;
	
	private Double totalAmount;
	
    @Column(name = "payment_status", nullable = false)
    private boolean paymentStatus;
    
}
