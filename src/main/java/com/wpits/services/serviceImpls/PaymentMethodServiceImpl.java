package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PaymentMethodDto;
import com.wpits.entities.PaymentMethod;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.PaymentMethodRepository;
import com.wpits.services.PaymentMethodService;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService{
	
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto) {
		return mapper.map(paymentMethodRepository.save(mapper.map(paymentMethodDto, PaymentMethod.class)), PaymentMethodDto.class);
	}

	@Override
	public List<PaymentMethodDto> getALlPaymentMethod() {
		return paymentMethodRepository.findAll().stream().map(paymentMethod -> mapper.map(paymentMethod, PaymentMethodDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deletePaymentMethod(int paymentMethodId) {
		PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow( () -> new ResourceNotFoundException("payment method not found with given id !!"));		
		paymentMethodRepository.delete(paymentMethod);
	}

}
