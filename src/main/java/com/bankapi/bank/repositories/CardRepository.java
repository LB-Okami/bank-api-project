package com.bankapi.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapi.bank.enums.Brand;
import com.bankapi.bank.model.Card;

public interface CardRepository extends JpaRepository<Card, Long>{
    Card findByName(String name);

    Card findByCardNumber(String cardNumber);

    Card findByBrand(Brand brand);

    Card findByNameAndBrand(String name, Brand brand);
}
