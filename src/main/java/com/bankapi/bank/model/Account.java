package com.bankapi.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tb_account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal creditLimit;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private BigDecimal debt;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("account")
    private Card card;

    @OneToOne
    @JoinColumn(name = "client_id")
    @JsonManagedReference
    private Client client;
}
