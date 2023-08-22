package com.bankapi.bank.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {
    private Double creditLimit;

    private Double balance;
    
    @NotNull
    private Long clientId;
}
