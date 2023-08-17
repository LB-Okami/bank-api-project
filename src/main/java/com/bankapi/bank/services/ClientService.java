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

    public Client findClientById(Long id) {
        Optional<Client> clientById = clientRepository.findById(id);

        if(!clientById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return clientRepository.findById(id).get();
    }

    public Client findClientByCpf(String cpf) {
        Client clientByCpf = clientRepository.findByCpf(cpf);

        if(clientByCpf == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return clientRepository.findByCpf(cpf);
    }

    public Client createClient(Client client) {
        Client clientByCpf = clientRepository.findByCpf(client.getCpf());
        Client clientByEmail = clientRepository.findByEmail(client.getEmail());
        Client clientByPhone = clientRepository.findByPhone(client.getPhone());

        if(clientByCpf != null || clientByEmail != null || clientByPhone != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        client.setCreationDate(LocalDate.now());
        client.setLastUpdate(LocalDateTime.now());

        return clientRepository.save(client);
    }

    public Client updateClient(Client updatedClient, Long id) {
        Optional<Client> clientDatabase = clientRepository.findById(id);
        Client clientByCpf = clientRepository.findByCpf(updatedClient.getCpf());
        Client clientByEmail = clientRepository.findByEmail(updatedClient.getEmail());
        Client clientByPhone = clientRepository.findByPhone(updatedClient.getPhone());

        if(!clientDatabase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!updatedClient.getCpf().equals(clientDatabase.get().getCpf()) && clientByCpf != null ||
                !updatedClient.getEmail().equals(clientDatabase.get().getEmail()) && clientByEmail != null 
                || !updatedClient.getPhone().equals(clientDatabase.get().getPhone()) && clientByPhone != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Client client = clientDatabase.get();

        client.setName(updatedClient.getName());
        client.setEmail(updatedClient.getEmail());
        client.setCpf(updatedClient.getCpf());
        client.setPhone(updatedClient.getPhone());
        client.setAddress(updatedClient.getAddress());

        client.setCreationDate(clientDatabase.get().getCreationDate());
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
