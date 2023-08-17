package com.bankapi.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapi.bank.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
    Client findByName(String name);

    Client findByCpf(String cpf);

    Client findByEmail(String email);

    Client findByPhone(String phone);
}
