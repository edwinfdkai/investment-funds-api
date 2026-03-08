package com.test.bank.repository;


import com.test.bank.model.Client;

public interface ClientRepository {

    Client findById(String id);

    void save(Client client);
}
