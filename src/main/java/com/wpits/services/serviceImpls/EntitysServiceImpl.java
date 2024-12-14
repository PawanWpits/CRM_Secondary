package com.wpits.services.serviceImpls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.EntitysDto;
import com.wpits.entities.Currency;
import com.wpits.entities.Entitys;
import com.wpits.entities.Language;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.EntitysRepository;
import com.wpits.repositories.LanguageRepository;
import com.wpits.services.EntitysService;

@Service
public class EntitysServiceImpl implements EntitysService{
	
	@Autowired
	private EntitysRepository entitysRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public EntitysDto createEntitys(EntitysDto entitysDto,int currencyId,int languageId) {
		entitysDto.setDeleted(0);
		entitysDto.setInvoiceAsReseller(false);
		
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () -> new ResourceNotFoundException("currency not found with given id !!"));
		
		Language language = languageRepository.findById(languageId).orElseThrow(() -> new ResourceNotFoundException("language not found with given id !!"));
		
		Entitys entitys = mapper.map(entitysDto, Entitys.class);
		
		entitys.setCurrency(currency);
		entitys.setLanguage(language);
		entitys.setCreateDatetime(LocalDateTime.now());
		entitys.setOptlock(new Random().nextInt(999999));
		
		Entitys savedEntitys = entitysRepository.save(entitys);
		
		return mapper.map(savedEntitys, EntitysDto.class);
	}

	@Override
	public EntitysDto updateEntitys(EntitysDto entitysDto, int entityId) {
		
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		entitys.setExternalId(entitysDto.getExternalId());
		entitys.setDescription(entitysDto.getDescription());
		entitys.setCreateDatetime(LocalDateTime.now());
		entitys.setOptlock(entitysDto.getOptlock());
		entitys.setDeleted(entitysDto.getDeleted());
		entitys.setInvoiceAsReseller(entitysDto.getInvoiceAsReseller());
		
		Entitys savedEntity = entitysRepository.save(entitys);
		return mapper.map(savedEntity, EntitysDto.class);
	}

	@Override
	public List<EntitysDto> getAllEntitys() {
		
		List<Entitys> entitys = entitysRepository.findAll();
		List<EntitysDto> entityDto = entitys.stream().map( entity -> mapper.map(entity, EntitysDto.class)).collect(Collectors.toList());
		return entityDto;
	}

	@Override
	public EntitysDto getByIdEntitys(int entityId) {
		
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		return mapper.map(entitys, EntitysDto.class);
	}

	@Override
	public void deleteEntitys(int entityId) {
		
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		entitysRepository.delete(entitys);
		
	}

	
}
