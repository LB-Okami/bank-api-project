package com.bankapi.bank.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Card findCardById(Long id) {
        Optional<Card> cardById = cardRepository.findById(id);

        if(!cardById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return cardRepository.findById(id).get();
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

    public Card updateCard(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);

        Account accountById = accountService.findAccountById(updatedCardDTO.getAccountId());


        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!cardDatabase.get().getAccount().getId().equals(updatedCardDTO.getAccountId())) {
            checkIfAccountHasCard(updatedCardDTO.getAccountId());
        }

        Card card = cardDatabase.get();

        card.setName(updatedCardDTO.getName());
        card.setBrand(updatedCardDTO.getBrand());
        card.setLevel(updatedCardDTO.getLevel());
        card.setBill(updatedCardDTO.getBill());
        card.setCardNumber(updatedCardDTO.getCardNumber());
        card.setExpireDate(updatedCardDTO.getExpireDate());
        card.setCvv(updatedCardDTO.getCvv());
        card.setAccount(accountById);

        card.setCreationDate(cardDatabase.get().getCreationDate());
        card.setLastUpdate(LocalDateTime.now());

        return cardRepository.save(card);
    }

    public Card debitTransaction(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);

        Account accountById = accountService.findAccountById(updatedCardDTO.getAccountId());


        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!cardDatabase.get().getAccount().getId().equals(updatedCardDTO.getAccountId())) {
            checkIfAccountHasCard(updatedCardDTO.getAccountId());
        }

        debitTransactionFormula(cardDatabase.get().getAccount().getBalance(), updatedCardDTO.getValue());

        Card card = cardDatabase.get();
        
        card.setName(updatedCardDTO.getName());
        card.setBrand(updatedCardDTO.getBrand());
        card.setLevel(updatedCardDTO.getLevel());
        card.setBill(updatedCardDTO.getBill());
        card.setCardNumber(updatedCardDTO.getCardNumber());
        card.setExpireDate(updatedCardDTO.getExpireDate());
        card.setCvv(updatedCardDTO.getCvv());
        card.setAccount(accountById);

        card.setCreationDate(cardDatabase.get().getCreationDate());
        card.setLastUpdate(LocalDateTime.now());

        return cardRepository.save(card);
    }

    public Double debitTransactionFormula(Double balance, Double value) {
        if(value <= 0 || value > balance) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return balance - value;
    }

    public Card grantOrRemoveCreditAcess(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);

        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(updatedCardDTO.getName() != null && updatedCardDTO.getName().equals(cardDatabase.get().getName()) || 
                updatedCardDTO.getBrand() != null && !updatedCardDTO.getBrand().equals(cardDatabase.get().getBrand()) ||
                updatedCardDTO.getLevel() != null && !updatedCardDTO.getBrand().equals(cardDatabase.get().getBrand()) ||
                updatedCardDTO.getBill() != null && !updatedCardDTO.getBill().equals(cardDatabase.get().getBill()) || 
                updatedCardDTO.getCardNumber() != null && !updatedCardDTO.getCardNumber().equals(cardDatabase.get().getCardNumber()) ||
                updatedCardDTO.getExpireDate() != null && !updatedCardDTO.getExpireDate().equals(cardDatabase.get().getExpireDate()) ||
                updatedCardDTO.getCvv() != null && !updatedCardDTO.getCvv().equals(cardDatabase.get().getCvv()) ||
                updatedCardDTO.getAccountId() != null && !updatedCardDTO.getAccountId().equals(cardDatabase.get().getAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Card card = cardDatabase.get();

        card.setHasCreditAccess(updatedCardDTO.isHasCreditAccess());

        return cardRepository.save(card);
    }

    public void checkIfAccountHasCard(Long id) {
        Account accountById = accountService.findAccountById(id);

        if(accountById.getCard() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteCard(Long id) {
        Optional<Card> cardById = cardRepository.findById(id);

        if(!cardById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        cardRepository.deleteById(id);
    }
}
