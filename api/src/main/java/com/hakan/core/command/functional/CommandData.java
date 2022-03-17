package com.hakan.core.command.functional;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Command data class.
 */
public final class CommandData {

    private final CommandSender sender;
    private final String command;
    private final String[] args;

    /**
     * Creates new instance of this class.
     *
     * @param sender  Command sender.
     * @param command Command as string.
     * @param args    Arguments as string array.
     */
    public CommandData(@Nonnull CommandSender sender, @Nonnull String command, @Nonnull String... args) {
        this.sender = Objects.requireNonNull(sender, "sender cannot be null!");
        this.command = Objects.requireNonNull(command, "command cannot be null!");
        this.args = Objects.requireNonNull(args, "args cannot be null!");
    }

    /**
     * Gets sender of command.
     *
     * @return Sender of command.
     */
    @Nonnull
    public CommandSender getSender() {
        return this.sender;
    }

    /**
     * Gets name of command.
     *
     * @return Name of command.
     */
    @Nonnull
    public String getCommand() {
        return this.command;
    }

    /**
     * Gets arguments of command.
     *
     * @return Arguments of command.
     */
    @Nonnull
    public String[] getArgs() {
        return this.args;
    }
}