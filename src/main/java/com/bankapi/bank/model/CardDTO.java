package com.bankapi.bank.model;

import java.time.YearMonth;

import com.bankapi.bank.enums.Brand;
import com.bankapi.bank.enums.CurrencyType;
import com.bankapi.bank.enums.Level;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardDTO {
    @NotBlank
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    private Brand brand;

    @NotNull
    private CurrencyType currencyType;

    @NotNull
    private Level level;

    @NotBlank
    @Min(0)
    private Double bill;

    @Min(0)
    private Double value;

    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotNull
    private YearMonth expireDate;

    @NotBlank
    @Size(min = 3, max = 3)
    private String cvv;

    private boolean hasCreditAccess = false;

    @NotNull
    private Long accountId;
}
