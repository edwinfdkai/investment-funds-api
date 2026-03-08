package com.test.bank.repository;


import com.test.bank.model.Client;

public interface ClientRepository {

    Client findById(Long id);

    void save(Client client);
}
