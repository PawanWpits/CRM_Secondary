package com.wpits.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vendor")
@Getter
@Setter
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "first_name")
	private String firstName;
	
	private String maidenName;
	
	private String lastName;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "contact", nullable = false, unique = true)
	private String contact;
	
	private String address;
	
	@Column(name = "company_name", nullable = false, unique = true)
	private String companyName;
	
	private LocalDate createDate;
	
	private String token;
	
	private int userId; //base
	
}
