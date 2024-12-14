package com.wpits.services.serviceImpls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.ContactDto;
import com.wpits.entities.Contact;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.ContactRepository;
import com.wpits.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{
	
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public ContactDto createContact(ContactDto contactDto) {		
		Contact contact = mapper.map(contactDto, Contact.class);
		contact.setCreateDateTime(LocalDateTime.now());
		contact.setDeleted(0);
		contact.setNotificationInclude(1);
		
		/*Random rand = new Random(); int optLock = rand.nextInt(999999);*/
		contact.setOptlock(new Random().nextInt(999999));
		//contactRepository.save(contact);
		return mapper.map(contactRepository.save(contact), ContactDto.class);
	}

	@Override
	public ContactDto updateContact(ContactDto contactDto, int contactId) {
		Contact contact = contactRepository.findById(contactId).orElseThrow( () -> new ResourceNotFoundException("contact not found with given id !!"));
		contact.setOrganizationName(contactDto.getOrganizationName());
		contact.setStreetAddres1(contactDto.getStreetAddres1());
		contact.setStreetAddres2(contactDto.getStreetAddres2());
		contact.setCity(contactDto.getCity());
		contact.setStateProvince(contactDto.getStateProvince());
		contact.setPostalCode(contactDto.getPostalCode());
		contact.setCountryCode(contactDto.getCountryCode());
		contact.setLastName(contactDto.getLastName());
		contact.setFirstName(contactDto.getFirstName());
		contact.setPersonInitial(contactDto.getPersonInitial());
		contact.setPersonTitle(contactDto.getPersonTitle());
		contact.setPhoneCountryCode(contactDto.getPhoneCountryCode());
		contact.setPhoneAreaCode(contactDto.getPhoneAreaCode());
		contact.setPhonePhoneNumber(contactDto.getPhonePhoneNumber());
		contact.setFaxCountryCode(contactDto.getFaxCountryCode());
		contact.setFaxAreaCode(contactDto.getFaxAreaCode());
		contact.setFaxPhoneNumber(contactDto.getFaxPhoneNumber());
		contact.setEmail(contactDto.getEmail());
		contact.setCreateDateTime(LocalDateTime.now());
		contact.setDeleted(0);
		contact.setNotificationInclude(1);	
		contact.setUserId(contactDto.getUserId());
		contact.setOptlock(contactDto.getOptlock());
		return mapper.map(contactRepository.save(contact), ContactDto.class);
	}

	@Override
	public List<ContactDto> getAllContact() {
		return contactRepository.findAll().stream().map( contact -> mapper.map(contact, ContactDto.class)).collect(Collectors.toList());
	}

	@Override
	public ContactDto findByIdContact(int contactId) {
		Contact contact = contactRepository.findById(contactId).orElseThrow( () -> new ResourceNotFoundException("contact not found with given id !!"));
		return mapper.map(contact, ContactDto.class);
	}

	@Override
	public void deleteContact(int contactId) {
		Contact contact = contactRepository.findById(contactId).orElseThrow( () -> new ResourceNotFoundException("contact not found with given id !!"));
		contactRepository.delete(contact);
	}

}
