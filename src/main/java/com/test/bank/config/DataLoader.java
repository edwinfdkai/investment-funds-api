package com.test.bank.config;

import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;

    @Override
    public void run(String... args) {

        Client client = new Client(
                "1",
                "Edwin Caicedo",
                "edwinfdfull@gmail.com",
                "+57300123456",
                500000L,
                "EMAIL"
        );

        clientRepository.save(client);

        fundRepository.save(new Fund("1","PRU_TEST_PACTUAL_RECAUDADORA",75000L,"PRU"));
        fundRepository.save(new Fund("2","PRU_TEST_PACTUAL_ECOPETROL",125000L,"PRU"));
        fundRepository.save(new Fund("3","DEUDAPRIVADA",50000L,"TIC"));
        fundRepository.save(new Fund("4","FDO-ACCIONES",250000L,"TIC"));
        fundRepository.save(new Fund("5","PRU_TEST_PACTUAL_DINAMICA",100000L,"PRU"));
    }
}