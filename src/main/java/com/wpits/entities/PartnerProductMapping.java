package com.wpits.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "partner_product_mapping")
@Getter
@Setter
public class PartnerProductMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "product_id")
	private int productId;
	
	@Column(name = "commission_type")
	private String commissionType;
	
	@Column(name = "commission_value")
	private String commissionValue;
	
	@ManyToOne
	@JoinColumn(name = "partner_id")
	private Partner partner; 
	
}
