package com.wpits.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

}
