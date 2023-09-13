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
import com.bankapi.bank.enums.Level;
import com.bankapi.bank.enums.Operation;
import com.bankapi.bank.model.Account;
import com.bankapi.bank.model.Card;
import com.bankapi.bank.model.CardDTO;
import com.bankapi.bank.repositories.CardRepository;

import lombok.val;

@Service
public class CardService {
    private final CardRepository cardRepository;

    private final AccountService accountService;

    private final ReportService reportService;

    private final CurrencyTypeAPIService apiService;

    @Autowired
    public CardService(CardRepository cardRepository, AccountService accountService, ReportService reportService, CurrencyTypeAPIService apiService) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.reportService = reportService;
        this.apiService = apiService;
    
    }

    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    public Card findCardById(Long id) {
        Optional<Card> cardById = cardRepository.findById(id);

        if(!cardById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card ID not found");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
        }
        else if(!cardDatabase.get().isHasCreditAccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card does not have credit access");
        }
        else if(!cardDatabase.get().getAccount().getId().equals(updatedCardDTO.getAccountId())) {
            checkIfAccountHasCard(updatedCardDTO.getAccountId());
        }

        Card card = cardDatabase.get();

        Double oldCardBill =  card.getBill();

        Double newCardBill;

        setCardAttributes(card, updatedCardDTO);
        
        if(updatedCardDTO.getCurrencyType() != null && updatedCardDTO.getCurrencyType().equals(currencyType)) {
           updatedCardDTO.setValue(updatedCardDTO.getValue() * apiService.fetchAPIData());
        }

        newCardBill = creditTransactionFormula(oldCardBill, updatedCardDTO.getValue(), card.getAccount().getCreditLimit(), card.getAccount().getId());

        card.setBill(newCardBill);

        milesPointsFormula(updatedCardDTO.getValue(), card.getAccount().getMilesPoints(), card.getLevel().toString());

        reportService.createReportFromCard(updatedCardDTO.getValue(), operationType, card);

        return cardRepository.save(card);
    }

    public Card grantOrRemoveCreditAcess(CardDTO updatedCardDTO, Long id) {
        Optional<Card> cardDatabase = cardRepository.findById(id);

        if(!cardDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
        }
        else if(checkIfCardWasAltered(cardDatabase.get(), updatedCardDTO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot alter card attributes");
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
        if(updatedCardDTO.getName().equals(card.getName()) || 
                !updatedCardDTO.getBrand().equals(card.getBrand()) ||
                !updatedCardDTO.getLevel().equals(card.getLevel()) ||
                !updatedCardDTO.getBill().equals(card.getBill()) || 
                !updatedCardDTO.getCardNumber().equals(card.getCardNumber()) ||
                !updatedCardDTO.getExpireDate().equals(card.getExpireDate()) ||
                !updatedCardDTO.getCvv().equals(card.getCvv()) ||
                !updatedCardDTO.getAccountId().equals(card.getAccount().getId())) {
                    return true;
        }
        return false;
    }

    public void checkIfAccountHasCard(Long id) {
        Account accountById = accountService.findAccountById(id);

        if(accountById.getCard() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This account alrealdy has a card");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value is negative or balance is not enough");
        }
        
        return balance - value;
    }

    public Double creditTransactionFormula(Double bill, Double value, Double creditLimit, Long id) {
        if(value <= 0 || value > creditLimit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value is negative or credit limit is not enough");
        }

        Double newCardBill = bill + value;

        Double newAccountCreditLimit = creditLimit - value;

        accountService.updateCreditLimit(newAccountCreditLimit, id);

        return newCardBill;
    }

    public Double milesPointsFormula(Double value, Double milesPoints, String cardLevel) {
        Level level = Level.valueOf(cardLevel);

        if(level == Level.GOLD && value >= 0.5) {
            return 0.0;
        }

        return milesPoints;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card not found");
        }

        cardRepository.deleteById(id);
    }
}
