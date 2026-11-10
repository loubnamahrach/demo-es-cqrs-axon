package com.example.demoescqrsaxon.dtos;

import com.example.demoescqrsaxon.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus accountStatus) {
}
