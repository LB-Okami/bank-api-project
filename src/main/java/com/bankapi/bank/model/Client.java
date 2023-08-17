package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "tb_client")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String cpf;

    @NotNull
    private String email;
    
    @NotNull
    private String phone;

    @NotNull
    private String address;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;
}
