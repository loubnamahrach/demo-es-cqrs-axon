package com.example.demoescqrsaxon.query.handlers;

import com.example.demoescqrsaxon.query.dtos.AccountStatementResponseDTO;
import com.example.demoescqrsaxon.query.entities.Account;
import com.example.demoescqrsaxon.query.entities.AccountOperation;
import com.example.demoescqrsaxon.query.queries.GetAccountStatementQuery;
import com.example.demoescqrsaxon.query.queries.GetAllAccountsQuery;
import com.example.demoescqrsaxon.query.queries.WatchEventQuery;
import com.example.demoescqrsaxon.query.repository.AccountOperationRepository;
import com.example.demoescqrsaxon.query.repository.AccountRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, AccountOperationRepository accountOperationRepository) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query) {
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> accountOperations = accountOperationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(account, accountOperations);
    }

    @QueryHandler
    public AccountOperation on(WatchEventQuery query) {
        return AccountOperation.builder().build();
    }
}
