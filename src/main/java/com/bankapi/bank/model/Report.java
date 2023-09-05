package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bankapi.bank.enums.Operation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tb_report")
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Operation operation;

    @NotNull
    @Min(0)
    private Double value;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private Card card;
}
