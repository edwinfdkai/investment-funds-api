package com.test.bank.controller;

import com.test.bank.dto.response.ApiResponse;
import com.test.bank.model.Fund;
import com.test.bank.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Fund>>> listFunds() {
        return ResponseEntity.ok(
                ApiResponse.success("Funds retrieved successfully", fundService.getFunds()));
    }
}
