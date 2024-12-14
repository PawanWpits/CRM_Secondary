package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>{

	Optional<CreditCard> findByCcNumber(String number);
}
