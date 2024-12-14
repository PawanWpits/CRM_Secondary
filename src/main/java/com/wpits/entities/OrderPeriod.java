package com.wpits.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_period")
@Getter
@Setter
public class OrderPeriod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "value")
	private int value;
	
	@Column(name = "optlock", nullable = false)
	private int optlock;
	
	@ManyToOne
	@JoinColumn(name = "entity_id")
	private Entitys entitys;
	
	@ManyToOne
	@JoinColumn(name = "unit_id")
	private PeriodUnit periodUnit;
	
	@OneToMany(mappedBy = "orderPeriod",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<AccountType> accountType;
	
	@OneToMany(mappedBy = "orderPeriod",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Customer> customers;
}
