package com.bankapi.bank.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bankapi.bank.model.Client;
import com.bankapi.bank.repositories.ClientRepository;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client createClient(Client client) {
        Client clientByName = clientRepository.findByName(client.getName());
        Client clientByCpf = clientRepository.findByCpf(client.getCpf());
        Client clientByEmail = clientRepository.findByEmail(client.getEmail());
        Client clientByPhone = clientRepository.findByPhone(client.getPhone());

        if(clientByName != null || clientByCpf != null || clientByEmail != null || clientByPhone != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        client.setCreationDate(LocalDate.now());
        client.setLastUpdate(LocalDateTime.now());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Optional<Client> clientById = clientRepository.findById(id);


        if(!clientById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        clientRepository.deleteById(id);
    }
}
