package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.OrderPeriodDto;
import com.wpits.entities.Entitys;
import com.wpits.entities.OrderPeriod;
import com.wpits.entities.PeriodUnit;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.EntitysRepository;
import com.wpits.repositories.OrderPeriodRepository;
import com.wpits.repositories.PeriodUnitRepository;
import com.wpits.services.OrderPeriodService;

@Service
public class OrderPeriodServiceImpl implements OrderPeriodService{
	
	@Autowired
	private OrderPeriodRepository orderPeriodRepository;
	@Autowired
	private EntitysRepository entitysRepository;
	@Autowired
	private PeriodUnitRepository periodUnitRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public OrderPeriodDto createOrderPeriod(OrderPeriodDto orderPeriodDto, int entityId, int periodUnitId) {
		
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		
		PeriodUnit periodUnit = periodUnitRepository.findById(periodUnitId).orElseThrow( () -> new ResourceNotFoundException("period unit not found with given id !!"));
		
		OrderPeriod orderPeriod = mapper.map(orderPeriodDto, OrderPeriod.class);
		orderPeriod.setEntitys(entitys);
		orderPeriod.setPeriodUnit(periodUnit);
		orderPeriod.setOptlock(new Random().nextInt(999999));
		OrderPeriod savedOrderPeriod = orderPeriodRepository.save(orderPeriod);
		return mapper.map(savedOrderPeriod, OrderPeriodDto.class);
	}

		@Override
		public OrderPeriodDto updateOrderPeriod(OrderPeriodDto orderPeriodDto, int orederPeriodId) {
			
			OrderPeriod orderPeriod = orderPeriodRepository.findById(orederPeriodId).orElseThrow( () -> new ResourceNotFoundException("order period not found with given id !!"));
			orderPeriod.setValue(orderPeriodDto.getValue());
			orderPeriod.setOptlock(orderPeriodDto.getOptlock());
			OrderPeriod savedOrderPeriod = orderPeriodRepository.save(orderPeriod);
			return mapper.map(savedOrderPeriod, OrderPeriodDto.class);
		}
	
	@Override
	public List<OrderPeriodDto> getAllOrderPeriod() {
		
		//List<OrderPeriod> orders = orderPeriodRepository.findAll();
		//List<OrderPeriodDto> orderDtos = orders.stream().map( order -> mapper.map(order, OrderPeriodDto.class)).collect(Collectors.toList());		
		return orderPeriodRepository.findAll().stream().map( order -> mapper.map(order, OrderPeriodDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteOrderPeriod(int orederPeriodId) {
		
		OrderPeriod orderPeriod = orderPeriodRepository.findById(orederPeriodId).orElseThrow( () -> new ResourceNotFoundException("order period not found with given id !!"));
		orderPeriodRepository.delete(orderPeriod);	
	}

}
