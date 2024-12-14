package com.wpits.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{

}
