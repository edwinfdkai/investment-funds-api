package com.test.bank.controller;

import com.test.bank.dto.response.ApiResponse;
import com.test.bank.model.Transaction;
import com.test.bank.service.TransactionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> listTransactions(
            @PathVariable @NotBlank(message = "clientId must not be blank") String clientId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Transactions retrieved successfully",
                        transactionService.history(clientId)));
    }
}
