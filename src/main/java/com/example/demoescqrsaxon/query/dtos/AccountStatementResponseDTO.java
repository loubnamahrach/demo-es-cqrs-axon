package com.example.demoescqrsaxon.query.dtos;

import com.example.demoescqrsaxon.query.entities.Account;
import com.example.demoescqrsaxon.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account, List<AccountOperation> accountOperations
) {
}
