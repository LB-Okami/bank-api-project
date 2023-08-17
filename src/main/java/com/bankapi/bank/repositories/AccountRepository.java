package com.bankapi.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapi.bank.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    
}
