package com.example.demoescqrsaxon.dtos;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}
