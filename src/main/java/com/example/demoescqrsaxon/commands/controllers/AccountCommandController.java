package com.example.demoescqrsaxon.commands.controllers;

import com.example.demoescqrsaxon.commands.commands.AddAccountCommand;
import com.example.demoescqrsaxon.commands.commands.CreditAccountCommand;
import com.example.demoescqrsaxon.commands.commands.DebitAccountCommand;
import com.example.demoescqrsaxon.commands.commands.UpdateAccountStatusCommand;
import com.example.demoescqrsaxon.dtos.AddNewAccountRequestDTO;
import com.example.demoescqrsaxon.dtos.CreditAccountRequestDTO;
import com.example.demoescqrsaxon.dtos.DebitAccountRequestDTO;
import com.example.demoescqrsaxon.dtos.UpdateAccountStatusRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandController {

    private CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountRequestDTO request) {
        CompletableFuture<String> response = commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.initialBalance(),
                request.currency()
        ));
        return response;
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request) {
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request) {
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }

    @PutMapping("/updateStatus")
    public CompletableFuture<String> updateStatus(@RequestBody UpdateAccountStatusRequestDTO request) {
        CompletableFuture<String> response = commandGateway.send(new UpdateAccountStatusCommand(
                request.accountId(),
                request.accountStatus()
        ));
        return response;
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception) {
        return exception.getMessage();
    }
}
