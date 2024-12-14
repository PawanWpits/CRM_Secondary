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
import com.wpits.dtos.ContactDto;
import com.wpits.services.ContactService;

@RestController
@RequestMapping("/api")
public class ContactController {

	@Autowired
	private ContactService contactService;
	
	@PostMapping("/savecontact")
	public ResponseEntity<ContactDto> createContact(@RequestBody ContactDto contactDto){
		return new ResponseEntity<ContactDto>(contactService.createContact(contactDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatecontact/{contactId}")
	public ResponseEntity<ContactDto> updateContact(@RequestBody ContactDto contactDto, @PathVariable int contactId){
		return ResponseEntity.ok(contactService.updateContact(contactDto, contactId));
	}
	
	@GetMapping("/contacts")
	public ResponseEntity<List<ContactDto>> getAllContacts(){
		return ResponseEntity.ok(contactService.getAllContact());
	}
	
	@GetMapping("/contact/{contactId}")
	public ResponseEntity<ContactDto> singleContact(@PathVariable int contactId){
		return ResponseEntity.ok(contactService.findByIdContact(contactId));
	}
	
	@DeleteMapping("/deletecontact/{contactId}")
	public ResponseEntity<ApiResponseMessage> deleteContact(@PathVariable int contactId){
		contactService.deleteContact(contactId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
