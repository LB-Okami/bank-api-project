package com.bankapi.bank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapi.bank.repositories.CardRepository;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
}
