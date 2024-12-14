package com.wpits.services;

import java.util.List;

import com.wpits.dtos.EntitysDto;

public interface EntitysService {
	
	EntitysDto createEntitys(EntitysDto entitysDto,int currencyId,int languageId);
	
	EntitysDto updateEntitys(EntitysDto entitysDto, int entityId);
	
	List<EntitysDto> getAllEntitys();
	
	EntitysDto getByIdEntitys(int entityId);
	
	void deleteEntitys(int entityId);

}
