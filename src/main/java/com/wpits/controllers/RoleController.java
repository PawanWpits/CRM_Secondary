package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.RoleDto;
import com.wpits.services.RoleService;

@RestController
@RequestMapping("/api")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@PostMapping("/saverole")
	public ResponseEntity<RoleDto> saveRole(@RequestBody RoleDto roleDto){
		return new ResponseEntity<RoleDto>(roleService.createRole(roleDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<List<RoleDto>> getAllRole(){
		return ResponseEntity.ok(roleService.getAllRole());
	}
}
