package com.bankapi.bank.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bankapi.bank.model.Account;
import com.bankapi.bank.model.Card;
import com.bankapi.bank.model.CardDTO;
import com.bankapi.bank.repositories.CardRepository;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountService accountService;

    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    public Card createCard(CardDTO cardDTO) {
        Card card = new Card();

        Account accountById = accountService.findAccountById(cardDTO.getAccountId());

        checkIfAccountHasCard(cardDTO.getAccountId());
        
        card.setName(cardDTO.getName());
        card.setBrand(cardDTO.getBrand());
        card.setLevel(cardDTO.getLevel());
        card.setBill(cardDTO.getBill());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setExpireDate(cardDTO.getExpireDate());
        card.setCvv(cardDTO.getCvv());
        card.setAccount(accountById);
        
        card.setCreationDate(LocalDate.now());
        card.setLastUpdate(LocalDateTime.now());

        return cardRepository.save(card);
    }

    public void checkIfAccountHasCard(Long id) {
        Account accountById = accountService.findAccountById(id);

        if(accountById.getCard() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
