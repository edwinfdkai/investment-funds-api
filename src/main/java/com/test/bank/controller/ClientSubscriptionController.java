package com.test.bank.controller;

import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.dto.request.CreateSubscriptionRequest;
import com.test.bank.dto.response.ApiResponse;
import com.test.bank.service.FundService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/subscriptions")
@RequiredArgsConstructor
@Validated
public class ClientSubscriptionController {

    private final FundService fundService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSubscription(
            @PathVariable @NotBlank(message = "clientId must not be blank") String clientId,
            @Valid @RequestBody CreateSubscriptionRequest request) {

        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setClientId(clientId);
        subscriptionRequest.setFundId(request.getFundId());

        fundService.subscribeToFund(subscriptionRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subscription created successfully", null));
    }

    @DeleteMapping("/{fundId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubscription(
            @PathVariable @NotBlank(message = "clientId must not be blank") String clientId,
            @PathVariable @NotBlank(message = "fundId must not be blank") String fundId) {

        CancelRequest cancelRequest = new CancelRequest();
        cancelRequest.setClientId(clientId);
        cancelRequest.setFundId(fundId);

        fundService.cancelFund(cancelRequest);

        return ResponseEntity.ok(ApiResponse.success("Subscription canceled successfully", null));
    }
}
