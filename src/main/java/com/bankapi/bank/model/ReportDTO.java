package com.bankapi.bank.model;

import com.bankapi.bank.enums.Operation;

import lombok.Data;

@Data
public class ReportDTO {
    private Operation operation;

    private Double value;

    private Long cardId;
}
