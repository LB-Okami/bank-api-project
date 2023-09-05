package com.bankapi.bank.model;

import lombok.Data;
@Data
public class AccountDTO {
    private Double creditLimit;

    private Double balance;

    private Long clientId;
}
