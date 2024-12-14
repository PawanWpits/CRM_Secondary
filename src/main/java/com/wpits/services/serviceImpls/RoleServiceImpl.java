package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.RoleDto;
import com.wpits.entities.Role;
import com.wpits.repositories.RoleRepository;
import com.wpits.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public RoleDto createRole(RoleDto roleDto) {
		return mapper.map(roleRepository.save(mapper.map(roleDto, Role.class)), RoleDto.class);
	}

	@Override
	public List<RoleDto> getAllRole() {
		return roleRepository.findAll().stream().map( role -> mapper.map(role, RoleDto.class)).collect(Collectors.toList());
	}

}
