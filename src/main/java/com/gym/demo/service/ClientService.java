package com.gym.demo.service;

import com.gym.demo.model.Client;
import com.gym.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Page<Client> findAllPageable(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Page<Client> findContainingIgnoreCase(String keyword, Pageable pageable) {
        if(keyword.equals("")){
            return clientRepository.findAll(pageable);
        }
        return clientRepository.findByNameOrSurnameContainingIgnoreCase(keyword, pageable);
    }

    public void updateClient(Client client) {
        if (clientRepository.existsById(client.getId())) {
            clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("Client with ID " + client.getId() + " does not exist.");
        }
    }
}
