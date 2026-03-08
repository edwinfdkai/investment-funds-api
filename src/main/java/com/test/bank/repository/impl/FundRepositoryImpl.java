package com.test.bank.repository.impl;


import com.test.bank.model.Fund;
import com.test.bank.repository.FundRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FundRepositoryImpl implements FundRepository {

    private final Map<Long, Fund> funds = new HashMap<>();

    @Override
    public Fund findById(Long id) {
        return funds.get(id);
    }

    @Override
    public void save(Fund fund) {
        funds.put(fund.getId(), fund);
    }

    @Override
    public List<Fund> findAll() {
        return new ArrayList<>(funds.values());
    }
}
