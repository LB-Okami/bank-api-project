package com.bankapi.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card findCardById(@PathVariable Long id) {
        return cardService.findCardById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Card createCard(@RequestBody CardDTO cardDTO) {
        return cardService.createCard(cardDTO);
    }

    @PatchMapping("/grantCreditAccess/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card grantOrRemoveCreditAcess(@RequestBody CardDTO updatedCardDTO, @PathVariable Long id) {
        return cardService.grantOrRemoveCreditAcess(updatedCardDTO, id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card updateCard(@RequestBody CardDTO updatedCardDTO, @PathVariable Long id) {
        return cardService.updateCard(updatedCardDTO, id);
    }

    @PutMapping("/debit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card debitTransaction(@RequestBody CardDTO updatedCardDTO, @PathVariable Long id) {
        return cardService.debitTransaction(updatedCardDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
