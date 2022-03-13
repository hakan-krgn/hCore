package com.hakan.command;

import org.bukkit.command.CommandSender;

public class CommandData {

    private final CommandSender sender;
    private final String command;
    private final String[] args;

    public CommandData(CommandSender sender, String command, String... args) {
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getCommand() {
        return this.command;
    }

    public String[] getArgs() {
        return this.args;
    }
}