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

        checkIfAccountHasCard(cardDTO.getAccountId());
        
        setCardAttributes(card, cardDTO);

        return cardRepository.save(card);
    }

    public Card updateCard(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);

        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!cardDatabase.get().getAccount().getId().equals(updatedCardDTO.getAccountId())) {
            checkIfAccountHasCard(updatedCardDTO.getAccountId());
        }

        Card card = cardDatabase.get();

        setCardAttributes(card, updatedCardDTO);

        return cardRepository.save(card);
    }

    public Card debitTransaction(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);


        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!cardDatabase.get().getAccount().getId().equals(updatedCardDTO.getAccountId())) {
            checkIfAccountHasCard(updatedCardDTO.getAccountId());
        }

        Double newAccountBalance = debitTransactionFormula(cardDatabase.get().getAccount().getBalance(), updatedCardDTO.getValue());

        Card card = cardDatabase.get();

        setCardAttributes(card, updatedCardDTO);

        accountService.updateAccountBalance(newAccountBalance, updatedCardDTO.getAccountId());

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
        else if(checkIfCardWasAltered(cardDatabase.get(), updatedCardDTO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Card card = cardDatabase.get();

        card.setHasCreditAccess(updatedCardDTO.isHasCreditAccess());

        return cardRepository.save(card);
    }

    public boolean checkIfCardWasAltered(Card card, CardDTO updatedCardDTO) {
        if(updatedCardDTO.getName() != null && updatedCardDTO.getName().equals(card.getName()) || 
                updatedCardDTO.getBrand() != null && !updatedCardDTO.getBrand().equals(card.getBrand()) ||
                updatedCardDTO.getLevel() != null && !updatedCardDTO.getBrand().equals(card.getBrand()) ||
                updatedCardDTO.getBill() != null && !updatedCardDTO.getBill().equals(card.getBill()) || 
                updatedCardDTO.getCardNumber() != null && !updatedCardDTO.getCardNumber().equals(card.getCardNumber()) ||
                updatedCardDTO.getExpireDate() != null && !updatedCardDTO.getExpireDate().equals(card.getExpireDate()) ||
                updatedCardDTO.getCvv() != null && !updatedCardDTO.getCvv().equals(card.getCvv()) ||
                updatedCardDTO.getAccountId() != null && !updatedCardDTO.getAccountId().equals(card.getAccount().getId())) {
                    return true;
        }
        return false;
    }

    public void checkIfAccountHasCard(Long id) {
        Account accountById = accountService.findAccountById(id);

        if(accountById.getCard() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Card setCardAttributes(Card card, CardDTO cardDTO) {

        Account accountById = accountService.findAccountById(cardDTO.getAccountId());

        card.setName(cardDTO.getName());
        card.setBrand(cardDTO.getBrand());
        card.setLevel(cardDTO.getLevel());
        card.setBill(cardDTO.getBill());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setExpireDate(cardDTO.getExpireDate());
        card.setCvv(cardDTO.getCvv());
        card.setAccount(accountById);
        if(card.getCreationDate() == null) {
            card.setCreationDate(LocalDate.now());
        }
        card.setLastUpdate(LocalDateTime.now());

        return card;
    }

    public void deleteCard(Long id) {
        Optional<Card> cardById = cardRepository.findById(id);

        if(!cardById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        cardRepository.deleteById(id);
    }
}
