package com.bankapi.bank.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {
    private double creditLimit;

    private Double balance;
    
    private Double debt;
    
    @NotNull
    private Long clientId;
}
