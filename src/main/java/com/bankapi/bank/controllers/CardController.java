package com.bankapi.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bankapi.bank.model.Card;
import com.bankapi.bank.model.CardDTO;
import com.bankapi.bank.services.CardService;

@RestController
@RequestMapping("/cards")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Card> findAllCards() {
        return cardService.findAllCards();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Card createCard(@RequestBody CardDTO cardDTO) {
        return cardService.createCard(cardDTO);
    }
}
