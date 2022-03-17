package com.hakan.core.command;

import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Sub command class to
 * register sub commands to
 * bukkit map.
 */
public class HSubCommand {

    protected final String name;
    protected final Map<String, HSubCommand> subCommands;

    /**
     * Creates new instance of this class.
     *
     * @param name Name of sub command.
     */
    public HSubCommand(@Nonnull String name) {
        this.name = Objects.requireNonNull(name, "sub command name cannot be null!");
        this.subCommands = new HashMap<>();
    }

    /**
     * Gets name of sub command.
     *
     * @return Name of sub command.
     */
    @Nonnull
    public final String getName() {
        return this.name;
    }

    /**
     * Registers a new sub command.
     *
     * @param subCommand Sub command name.
     * @return New instance of sub command.
     */
    @Nonnull
    public final HSubCommand subCommand(@Nonnull String subCommand) {
        Validate.notNull(subCommand, "sub command name cannot be null!");
        if (this.subCommands.containsKey(subCommand)) {
            return this.subCommands.get(subCommand);
        }
        HSubCommand hSubCommand = new HSubCommand(subCommand);
        this.subCommands.put(subCommand, hSubCommand);
        return hSubCommand;
    }

    /**
     * Gets sub command list from this sub command.
     *
     * @return Sub command list from this sub command.
     */
    @Nonnull
    public final Map<String, HSubCommand> getSubCommands() {
        return this.subCommands;
    }

    /**
     * Finds sub command from list.
     *
     * @param subCommand Sub command name.
     * @return Sub Command class as optional.
     */
    @Nonnull
    public final Optional<HSubCommand> findSubCommand(@Nonnull String subCommand) {
        return Optional.ofNullable(this.subCommands.get(Objects.requireNonNull(subCommand, "sub command name cannot be null!")));
    }

    /**
     * Gets sub command from list.
     *
     * @param subCommand Sub command name.
     * @return Sub Command class.
     */
    @Nonnull
    public final HSubCommand getSubCommand(String subCommand) {
        return this.findSubCommand(subCommand).orElseThrow(() -> new NullPointerException("there is no subcommand with this name!"));
    }
}