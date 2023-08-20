package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long id;

    @NotNull
    private String name;

    //fazer validação de cpf
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

    @OneToOne(mappedBy = "client", cascade = CascadeType.REMOVE)
    private Account account;
}
