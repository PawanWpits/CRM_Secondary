package com.wpits.services;

import java.util.List;

import com.wpits.dtos.ContactDto;

public interface ContactService {

	ContactDto createContact(ContactDto contactDto);
	
	ContactDto updateContact(ContactDto contactDto, int contactId);
	
	List<ContactDto> getAllContact();
	
	ContactDto findByIdContact(int contactId);
	
	void deleteContact(int contactId);
}
