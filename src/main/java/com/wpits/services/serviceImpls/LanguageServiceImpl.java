package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.LanguageDto;
import com.wpits.entities.Language;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.LanguageRepository;
import com.wpits.services.LanguageService;

@Service
public class LanguageServiceImpl implements LanguageService {
	
	@Autowired
	private LanguageRepository languageRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public LanguageDto createLanguage(LanguageDto languageDto) {
		
		Language language = mapper.map(languageDto, Language.class);
		Language savedLanguage = languageRepository.save(language);
		return mapper.map(savedLanguage, LanguageDto.class);
	}

	@Override
	public LanguageDto updateLanguage(LanguageDto languageDto, int languageId) {
		
		Language language = languageRepository.findById(languageId).orElseThrow( () -> new ResourceNotFoundException("language not found with given id !!"));
		
		language.setCode(languageDto.getCode());
		language.setDescription(languageDto.getDescription());
		Language savedLanguage = languageRepository.save(language);
		return mapper.map(savedLanguage, LanguageDto.class);
	}

	@Override
	public List<LanguageDto> getAllLanguage() {
		
		List<Language> languages = languageRepository.findAll();
		List<LanguageDto> languageDtos = languages.stream().map( language -> mapper.map(language, LanguageDto.class)).collect(Collectors.toList());
		return languageDtos;
	}

	@Override
	public void deleteLanguage(int languageId) {
		
		Language language = languageRepository.findById(languageId).orElseThrow( () -> new ResourceNotFoundException("language not found with given id !!"));
		languageRepository.delete(language);
	}

}
