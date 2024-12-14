package com.wpits.services;

import java.util.List;

import com.wpits.dtos.LanguageDto;

public interface LanguageService {
	
	LanguageDto createLanguage(LanguageDto languageDto);
	
	LanguageDto updateLanguage(LanguageDto languageDto, int languageId);
	
	List<LanguageDto> getAllLanguage();
	
	void deleteLanguage(int languageId);
}
