package com.test.bank.controller;


import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.dto.response.ApiResponse;
import com.test.bank.model.Fund;
import com.test.bank.model.Transaction;
import com.test.bank.service.FundService;
import com.test.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;
    private final TransactionService transactionService;

    @PostMapping("/subscription")
    public ResponseEntity<ApiResponse> subscription(@RequestBody SubscriptionRequest request){

        fundService.subscribeToFund(request);

        return ResponseEntity.ok(
                new ApiResponse(
                        "SUCCESS",
                        "Subscription created successfully"
                )
        );
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse> cancel(@RequestBody CancelRequest request){

        fundService.cancelFund(request);

        return ResponseEntity.ok(
                new ApiResponse(
                        "CANCEL",
                        "Subscription canceled successfully"
                )
        );
    }

    @GetMapping("/{id}/transacciones")
    public List<Transaction> history(@PathVariable String id){

        return transactionService.history(id);
    }

    @GetMapping
    public List<Fund> getFunds(){
        return fundService.getFunds();
    }
}
