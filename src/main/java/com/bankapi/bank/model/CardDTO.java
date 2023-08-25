package com.bankapi.bank.model;

import java.time.YearMonth;

import com.bankapi.bank.enums.Brand;
import com.bankapi.bank.enums.CurrencyType;
import com.bankapi.bank.enums.Level;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CardDTO {
    private String name;

    private Brand brand;

    @NotNull
    private CurrencyType currencyType;

    private Level level;

    private Double bill;

    private Double value;

    private String cardNumber;

    private YearMonth expireDate;

    private String cvv;

    private boolean hasCreditAccess = false;

    @NotNull
    private Long accountId;
}
