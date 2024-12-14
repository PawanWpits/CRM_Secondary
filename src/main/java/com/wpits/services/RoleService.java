package com.wpits.services;

import java.util.List;

import com.wpits.dtos.RoleDto;

public interface RoleService {

	RoleDto createRole(RoleDto roleDto);
	
	List<RoleDto> getAllRole();
}
