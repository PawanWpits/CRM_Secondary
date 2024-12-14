package com.wpits.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "language")
@Getter
@Setter
public class Language {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@OneToMany(mappedBy = "language", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Entitys> entitys;
	
	@OneToMany(mappedBy = "language", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<AccountType> accountTypes;
	
	@OneToMany(mappedBy = "language", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<BaseUser> baseUser;
	
}
