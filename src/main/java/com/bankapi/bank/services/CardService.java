package com.bankapi.bank.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bankapi.bank.enums.CurrencyType;
import com.bankapi.bank.enums.Operation;
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

    @Autowired
    private ReportService reportService;

    @Autowired
    private CurrencyTypeAPIService apiService;

    public List<Card> findAllCards() {
        apiService.fetchAPIData();
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

        Operation operationType = Operation.DEBIT;

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

        reportService.createReportFromCard(updatedCardDTO.getValue(), operationType, card);

        return cardRepository.save(card);
    }

    public Card creditTransaction(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);

        CurrencyType currencyType = CurrencyType.USD;

        Operation operationType = Operation.CREDIT;

        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!cardDatabase.get().isHasCreditAccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else if(!cardDatabase.get().getAccount().getId().equals(updatedCardDTO.getAccountId())) {
            checkIfAccountHasCard(updatedCardDTO.getAccountId());
        }

        Card card = cardDatabase.get();

        Double oldCardBill =  card.getBill();

        Double newCardBill;


        setCardAttributes(card, updatedCardDTO);
        
        if(updatedCardDTO.getCurrencyType().equals(currencyType)) {
           updatedCardDTO.setValue(updatedCardDTO.getValue() * apiService.fetchAPIData());
        }

        newCardBill = creditTransactionFormula(oldCardBill, updatedCardDTO.getValue(), card.getAccount().getCreditLimit(), card.getAccount().getId());
        

        card.setBill(newCardBill);

        reportService.createReportFromCard(updatedCardDTO.getValue(), operationType, card);

        return cardRepository.save(card);
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

        Double newAccountCreditLimit = creditLimitFormula(card.getAccount().getBalance());

        card.setHasCreditAccess(updatedCardDTO.isHasCreditAccess());
        
        if(card.isHasCreditAccess()) {
            accountService.updateCreditLimit(newAccountCreditLimit, card.getAccount().getId());
        }

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

    public void checkCurrencyTypeAndUpdateValues(CardDTO cardDto, Card card) {
        CurrencyType usdCurrency = CurrencyType.USD;

        if(cardDto.getCurrencyType().equals(usdCurrency)) {
           card.setValue(card.getValue() * apiService.fetchAPIData());
           card.setBill(card.getBill() * apiService.fetchAPIData());  
        }
    }

    public Double debitTransactionFormula(Double balance, Double value) {
        if(value <= 0 || value > balance) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        
        return balance - value;
    }

    public Double creditTransactionFormula(Double bill, Double value, Double creditLimit, Long id) {
        if(value <= 0 || value > creditLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Double newCardBill = bill + value;

        Double newAccountCreditLimit = creditLimit - value;

        accountService.updateCreditLimit(newAccountCreditLimit, id);

        return newCardBill;
    }

    public Double creditLimitFormula(Double balance) {
        Double creditLimit;

        if(balance < 2000) {
            creditLimit = 1000.00;
        }
        else {
            creditLimit = balance * 0.02;
        }

        return creditLimit;
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
