package com.example.demoescqrsaxon.events;

import com.example.demoescqrsaxon.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountStatusUpdatedEvent {
    private String accountId;
    private AccountStatus status;
}
