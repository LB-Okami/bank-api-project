package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bankapi.bank.enums.Brand;
import com.bankapi.bank.enums.Level;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private Brand brand;

    private Level level;

    //Fazer validações de bandeira
    @Size(min = 16, max = 16)
    private String cardNumber;

    @NotNull
    private LocalDate expireDate;

    @Size(min = 3, max = 3)
    private String cvv;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    private boolean hasCreditAccess = false;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("card")
    private List<Report> report;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isHasCreditAccess() {
        return hasCreditAccess;
    }

    public void setHasCreditAccess(boolean hasCreditAccess) {
        this.hasCreditAccess = hasCreditAccess;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Report> getReport() {
        return report;
    }

    public void setReport(List<Report> report) {
        this.report = report;
    }
}
