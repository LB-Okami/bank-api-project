package com.bankapi.bank.model;

import com.bankapi.bank.enums.Operation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportDTO {
    @NotNull
    private Operation operation;

    @NotNull
    @Min(0)
    private Double value;

    @NotNull
    private Long cardId;
}
