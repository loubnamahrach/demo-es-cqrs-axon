package com.example.demoescqrsaxon.dtos;

public record CreditAccountRequestDTO(String accountId, double amount, String currency) {
}
