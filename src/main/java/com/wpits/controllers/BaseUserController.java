package com.wpits.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.BaseUserDto;
import com.wpits.services.BaseUserService;

@RestController
@RequestMapping("/api")
public class BaseUserController {

	@Autowired
	private BaseUserService baseUserService;
	
	@PostMapping("/savebaseuser/entity/{entityId}/language/{languageId}/currency/{currencyId}/userstatus/{userStatusId}")
	public ResponseEntity<BaseUserDto> createBaseUser(@Valid @RequestBody BaseUserDto baseUserDto,@PathVariable int entityId,@PathVariable int languageId,@PathVariable int currencyId,@PathVariable int userStatusId ){
		return new ResponseEntity<>(baseUserService.createBaseUser(baseUserDto, entityId, languageId, currencyId, userStatusId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatebaseuser/{baseUserId}")
	public ResponseEntity<BaseUserDto> updateBaseUser(@RequestBody BaseUserDto baseUserDto,@PathVariable int baseUserId){
		return ResponseEntity.ok(baseUserService.updateBaseUser(baseUserDto, baseUserId));
	}
	
	@GetMapping("/baseusers")
	public ResponseEntity<List<BaseUserDto>> getAllBaseUser(){
		return ResponseEntity.ok(baseUserService.getAllBaseUser());
	}
	
	@DeleteMapping("/deletebaseuser/{baseUserId}")
	public ResponseEntity<ApiResponseMessage> deleteBaseUser(@PathVariable int baseUserId){
		baseUserService.deleteBaseUser(baseUserId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
