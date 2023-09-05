package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.bankapi.bank.enums.Brand;
import com.bankapi.bank.enums.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    private Brand brand;

    @NotNull
    private Level level;

    @NotBlank
    @Min(0)
    private Double bill = 0.0;

    @Min(0)
    private Double value;

    //Fazer validações de bandeira
    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotNull
    private YearMonth expireDate;

    @NotBlank
    @Size(min = 3, max = 3)
    private String cvv;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    private boolean hasCreditAccess = false;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<Report> reports = new ArrayList<>();
}
