package com.bankapi.bank.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {
    private BigDecimal creditLimit;

    private BigDecimal balance;
    
    private BigDecimal debt;
    
    @NotNull
    private Long clientId;
}
