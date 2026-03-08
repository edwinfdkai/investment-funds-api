package com.test.bank.repository;

import com.test.bank.model.Fund;

import java.util.List;

public interface FundRepository {

    Fund findById(String id);

    void save(Fund fund);

    List<Fund> findAll();
}
