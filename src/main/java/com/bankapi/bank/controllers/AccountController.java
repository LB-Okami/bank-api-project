package com.bankapi.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bankapi.bank.model.Account;
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
}
