package com.hakan.core.command;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HSubCommand {

    protected final String name;
    protected final Map<String, HSubCommand> subCommands;

    public HSubCommand(String name) {
        this.name = name;
        this.subCommands = new HashMap<>();
    }

    @Nonnull
    public final String getName() {
        return this.name;
    }

    public final HSubCommand subCommand(String subCommand) {
        if (this.subCommands.containsKey(subCommand)) {
            return this.subCommands.get(subCommand);
        }
        HSubCommand hSubCommand = new HSubCommand(subCommand);
        this.subCommands.put(subCommand, hSubCommand);
        return hSubCommand;
    }

    public final Map<String, HSubCommand> getSubCommands() {
        return this.subCommands;
    }

    public final Optional<HSubCommand> findSubCommand(String subCommand) {
        return Optional.ofNullable(this.subCommands.get(subCommand));
    }

    public final HSubCommand getSubCommand(String subCommand) {
        return this.findSubCommand(subCommand).orElseThrow(() -> new NullPointerException("there is no subcommand with this name!"));
    }
}