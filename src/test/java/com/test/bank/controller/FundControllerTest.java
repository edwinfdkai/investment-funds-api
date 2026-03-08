package com.test.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.model.Fund;
import com.test.bank.model.Transaction;
import com.test.bank.service.FundService;
import com.test.bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FundController.class)
@AutoConfigureMockMvc(addFilters = false)
class FundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundService fundService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSubscribeToFund() throws Exception {

        SubscriptionRequest request = new SubscriptionRequest();
        request.setClientId("1L");
        request.setFundId("100L");

        doNothing().when(fundService).subscribeToFund(request);

        mockMvc.perform(post("/funds/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Subscription created successfully"));
    }

    @Test
    void shouldCancelSubscription() throws Exception {

        CancelRequest request = new CancelRequest();
        request.setClientId("1L");
        request.setFundId("100L");

        doNothing().when(fundService).cancelFund(request);

        mockMvc.perform(delete("/funds/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCEL"))
                .andExpect(jsonPath("$.message").value("Subscription canceled successfully"));
    }

    @Test
    void shouldReturnFunds() throws Exception {

        Fund fund = new Fund("1L", "Fund A", (long) 500000.0, "FPV");

        when(fundService.getFunds()).thenReturn(List.of(fund));

        mockMvc.perform(get("/funds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fund A"));
    }

    @Test
    void shouldReturnTransactionHistory() throws Exception {

        Transaction transaction = new Transaction(
                "1",
                "1L",
                "100L",
                "OPENING",
                (long) 500000.0,
                null
        );

        when(transactionService.history("1")).thenReturn(List.of(transaction));

        mockMvc.perform(get("/funds/1/transacciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("OPENING"));
    }
}