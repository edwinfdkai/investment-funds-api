package com.test.bank.controller;

import com.test.bank.config.RestExceptionHandlerTestConfig;
import com.test.bank.model.Transaction;
import com.test.bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandlerTestConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void getTransactions_returnsWrappedHistory() throws Exception {
        Transaction transaction = new Transaction(
                "1",
                "1",
                "100L",
                "OPENING",
                500_000L,
                LocalDate.of(2026, 3, 28));

        when(transactionService.history("1")).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/v1/clients/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.data[0].type").value("OPENING"));
    }
}
