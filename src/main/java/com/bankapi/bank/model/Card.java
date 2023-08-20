package com.bankapi.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import com.bankapi.bank.enums.Brand;
import com.bankapi.bank.enums.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "tb_card")
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Brand brand;

    private Level level;

    private BigDecimal bill;

    //Fazer validações de bandeira
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotNull
    private YearMonth expireDate;

    @Size(min = 3, max = 3)
    private String cvv;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    private boolean hasCreditAccess = false;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonIgnore
    private Account account;

    //@OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    //@JsonIgnoreProperties("card")
    //@JsonIgnore
    //private List<Report> report;
}
