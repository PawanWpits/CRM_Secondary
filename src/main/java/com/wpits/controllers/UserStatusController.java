package com.wpits.controllers;

import java.util.List;

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
import com.wpits.dtos.UserStatusDto;
import com.wpits.services.UserStatusService;

@RestController
@RequestMapping("/api")
public class UserStatusController {

	@Autowired
	private UserStatusService userStatusService;
	
	@PostMapping("/saveuserstatus")
	public ResponseEntity<UserStatusDto> saveUserStatus(@RequestBody UserStatusDto userStatusDto){		
		return new ResponseEntity<UserStatusDto>(userStatusService.createUserStatus(userStatusDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/updateuserstatus/{userStatusId}")
	public ResponseEntity<UserStatusDto> updateUserStatus(@RequestBody UserStatusDto userStatusDto, @PathVariable int userStatusId){
		return ResponseEntity.ok(userStatusService.updateUserStatus(userStatusDto, userStatusId));
	}
	
	@GetMapping("/alluserstatus")
	public ResponseEntity<List<UserStatusDto>> getAllUserStatus(){
		return ResponseEntity.ok(userStatusService.getAllUserStatus());
	}
	
	@DeleteMapping("/deleteuserstatus/{userStatusId}")
	public ResponseEntity<ApiResponseMessage> deleteUserStatus(@PathVariable int userStatusId){
		userStatusService.deleteUserStatus(userStatusId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
