package com.wpits.services;

import java.util.List;

import com.wpits.dtos.OrderPeriodDto;

public interface OrderPeriodService {

	OrderPeriodDto createOrderPeriod(OrderPeriodDto orderPeriodDto,int entityId,int periodUnitId); //foriegn key entityId,periodUnitId
	
	OrderPeriodDto updateOrderPeriod(OrderPeriodDto orderPeriodDto,int orederPeriodId);
	
	List<OrderPeriodDto> getAllOrderPeriod();
	
	void deleteOrderPeriod(int orederPeriodId);
	
	
}
