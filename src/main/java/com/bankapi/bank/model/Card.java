package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bankapi.enums.Brand;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Brand brand;

    private String level;

    //Fazer validações de bandeira
    @Size(min = 16, max = 16)
    private String cardNumber;

    private LocalDate expireDate;

    private String cvv;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    private boolean hasCreditAccess;

    // TO DO [Fazer relacionamento com Account (OneToOne)]

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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
}
