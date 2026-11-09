package com.example.demoescqrsaxon.commands.commands;

@Getter
public class AddAccountCommand {
    private String id;
    private double initialBalance;
    private String currency;
}
