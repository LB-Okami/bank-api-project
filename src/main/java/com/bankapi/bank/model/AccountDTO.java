package com.bankapi.bank.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountDTO {

    @NotBlank
    @Min(0)
    private Double creditLimit;

    @NotBlank
    @Min(0)
    private Double balance;
    
    @NotBlank
    private Long clientId;
}
