package com.bankapi.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "tb_client")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @CPF(message = "Insira um CPF válido")
    private String cpf;

    @Email(message = "Insira um email válido")
    private String email;
    
    @NotBlank(message = "Número de telefone é obrigatório")
    @Min(10)
    @Max(11)
    private String phone;

    @NotBlank(message = "Endereço é obrigatório")
    private String address;

    private LocalDate creationDate;

    private LocalDateTime lastUpdate;

    @OneToOne(mappedBy = "client", cascade = CascadeType.REMOVE)
    private Account account;
}
