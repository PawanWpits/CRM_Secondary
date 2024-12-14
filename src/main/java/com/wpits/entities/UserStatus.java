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
@Table(name = "user_status")
@Getter
@Setter
public class UserStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "can_login")
	private Short canLogin;
	
	@OneToMany(mappedBy = "userStatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<BaseUser> baseUsers;
}
