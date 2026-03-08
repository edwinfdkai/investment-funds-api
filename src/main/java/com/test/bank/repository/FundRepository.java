package com.test.bank.repository;

import com.test.bank.model.Fund;

import java.util.List;

public interface FundRepository {

    Fund findById(Long id);

    void save(Fund fund);

    List<Fund> findAll();
}
