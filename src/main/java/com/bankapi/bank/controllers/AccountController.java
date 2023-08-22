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

import com.bankapi.bank.model.Account;
import com.bankapi.bank.model.AccountDTO;
import com.bankapi.bank.model.Card;
import com.bankapi.bank.model.CardDTO;
import com.bankapi.bank.services.AccountService;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAllAccounts() {
        return accountService.findAllAccounts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account findAccountById(@PathVariable Long id) {
        return accountService.findAccountById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO);
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account updateAccount(@RequestBody AccountDTO updatedAccountDTO, @PathVariable Long id) {
        return accountService.updateAccount(updatedAccountDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}
