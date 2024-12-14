package com.wpits.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_delivery_method")
@Getter
@Setter
public class InvoiceDeliveryMethod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany(mappedBy = "invoiceDeliveryMethod",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<AccountType> accountTypes;
	
	@OneToMany(mappedBy = "invoiceDeliveryMethod",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Customer> customers;
}
