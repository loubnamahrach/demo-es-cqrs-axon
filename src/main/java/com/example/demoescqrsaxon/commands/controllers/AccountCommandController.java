package com.example.demoescqrsaxon.commands.controllers;

import com.example.demoescqrsaxon.dtos.AddNewAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands/account")
public class AccountCommandController {

    @Autowired
    private CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }
    @PostMapping("/add")
    public String addNewAccount(@RequestBody AddNewAccountRequestDTO request){
        commandGateway.send();
    }

}
