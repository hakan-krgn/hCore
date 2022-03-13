package com.hakan.command;

import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class HCommand extends HCommandExecutor {

    private Consumer<CommandData> consumer;

    public HCommand(String command, String... aliases) {
        super(command, null, aliases);
    }

    public void onCommand(Consumer<CommandData> consumer) {
        this.consumer = consumer;
    }

    @Override
    protected void onCommand(CommandSender sender, String... args) {
        CommandData commandData = new CommandData(sender, this.getCommand(), args);
        if (this.consumer != null)
            this.consumer.accept(commandData);
    }
}