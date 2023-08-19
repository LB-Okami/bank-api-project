package com.bankapi.bank.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bankapi.bank.model.Account;
import com.bankapi.bank.model.Client;
import com.bankapi.bank.repositories.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public Account findAccountById(Long id) {
        Optional<Account> accountById = accountRepository.findById(id);

        if(!accountById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return accountRepository.findById(id).get();
    }

    public Account createAccount(Account account, Long clientId) {

        Client clientById = clientService.findClientById(clientId);

        account.setClient(clientById);
        account.setCreationDate(LocalDate.now());
        account.setLastUpdate(LocalDateTime.now());

        return accountRepository.save(account);
    }
}