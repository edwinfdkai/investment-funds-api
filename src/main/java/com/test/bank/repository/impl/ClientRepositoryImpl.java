package com.test.bank.repository.impl;

import com.test.bank.model.Client;
import com.test.bank.repository.ClientRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private final Map<Long, Client> clients = new HashMap<>();

    @Override
    public Client findById(Long id) {
        return clients.get(id);
    }

    @Override
    public void save(Client client) {
        clients.put(client.getId(), client);
    }


}
