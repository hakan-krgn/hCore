package com.hakan.core.command.executors.basecommand;

import com.hakan.core.command.executors.placeholder.PlaceholderData;
import com.hakan.core.command.executors.subcommand.SubCommandData;
import com.hakan.core.command.listeners.CommandListener;
import com.hakan.core.command.utils.CommandUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * BaseCommandData class to get datas
 * of main command from annotated class.
 */
@SuppressWarnings({"unchecked"})
public final class BaseCommandData {

    private final Object adapter;
    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;
    private final boolean tabComplete;
    private final CommandListener listener;
    private final List<SubCommandData> subCommands;
    private final List<PlaceholderData> placeholders;

    /**
     * Constructor to create BaseCommandData object.
     *
     * @param adapter     Annotated class.
     * @param baseCommand Annotation.
     */
    public BaseCommandData(@Nonnull Object adapter, @Nonnull BaseCommand baseCommand) {
        Validate.notNull(adapter, "adapter cannot be null!");
        Validate.notNull(baseCommand, "baseCommand cannot be null!");

        this.adapter = adapter;
        this.name = baseCommand.name();
        this.usage = baseCommand.usage();
        this.aliases = baseCommand.aliases();
        this.description = baseCommand.description();
        this.tabComplete = baseCommand.tabComplete();

        this.subCommands = new LinkedList<>();
        this.placeholders = new ArrayList<>();
        this.listener = new CommandListener(this);
    }

    /**
     * Gets adapter class from annotation.
     *
     * @return Adapter class from annotation.
     */
    @Nonnull
    public Object getAdapter() {
        return this.adapter;
    }

    /**
     * Gets command name from annotation.
     *
     * @return Command name from annotation.
     */
    @Nonnull
    public String getName() {
        return this.name;
    }

    /**
     * Gets command description from annotation.
     *
     * @return Command description from annotation.
     */
    @Nonnull
    public String[] getAliases() {
        return this.aliases;
    }

    /**
     * Gets command description from annotation.
     *
     * @return Command description from annotation.
     */
    @Nonnull
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets tab complete status from annotation.
     *
     * @return Tab complete status from annotation.
     */
    public boolean isTabComplete() {
        return this.tabComplete;
    }

    /**
     * Gets command usage from annotation.
     *
     * @return Command usage from annotation.
     */
    @Nonnull
    public String getUsage() {
        return this.usage;
    }

    /**
     * Gets sub commands from annotated class as safe.
     *
     * @return Sub commands from annotated class.
     */
    @Nonnull
    public List<SubCommandData> getSubCommandsSafe() {
        return new ArrayList<>(this.subCommands);
    }

    /**
     * Gets sub commands from annotated class.
     *
     * @return Sub commands from annotated class.
     */
    @Nonnull
    public List<SubCommandData> getSubCommands() {
        return this.subCommands;
    }

    /**
     * Gets placeholders from annotated class.
     *
     * @return Placeholders from annotated class.
     */
    @Nonnull
    public List<PlaceholderData> getPlaceholdersSafe() {
        return new ArrayList<>(this.placeholders);
    }

    /**
     * Gets placeholders from annotated class.
     *
     * @return Placeholders from annotated class.
     */
    @Nonnull
    public List<PlaceholderData> getPlaceholders() {
        return this.placeholders;
    }

    /**
     * Adds sub command.
     *
     * @param subCommand SubCommandData class.
     * @return Instance of this class.
     */
    @Nonnull
    public BaseCommandData addSubCommand(@Nonnull SubCommandData subCommand) {
        this.subCommands.add(Validate.notNull(subCommand, "subCommand cannot be null!"));
        Collections.sort(this.subCommands);
        return this;
    }

    /**
     * Removes sub command.
     *
     * @param subCommand SubCommandData class.
     * @return Instance of this class.
     */
    @Nonnull
    public BaseCommandData removeSubCommand(@Nonnull SubCommandData subCommand) {
        this.subCommands.remove(Validate.notNull(subCommand, "subCommand cannot be null!"));
        Collections.sort(this.subCommands);
        return this;
    }

    /**
     * Finds sub command by arguments.
     *
     * @param subCommands Sub command args.
     * @return SubCommandData as Optional.
     */
    @Nonnull
    public Optional<SubCommandData> findSubCommand(@Nonnull String[] subCommands) {
        Validate.notNull(subCommands, "subCommands cannot be null!");

        for (SubCommandData subCommandData : this.subCommands) {
            String[] commands = subCommandData.getArgs();
            if (CommandUtils.isMatching(commands, subCommands)) {
                return Optional.of(subCommandData);
            }
        }
        return Optional.empty();
    }

    /**
     * Adds placeholder.
     *
     * @param placeholder PlaceholderData class.
     */
    public void addPlaceholder(@Nonnull PlaceholderData placeholder) {
        this.placeholders.add(Validate.notNull(placeholder, "placeholder cannot be null!"));
    }

    /**
     * Removes placeholder.
     *
     * @param placeholder PlaceholderData class.
     */
    public void removePlaceholder(@Nonnull PlaceholderData placeholder) {
        this.placeholders.remove(Validate.notNull(placeholder, "placeholder cannot be null!"));
    }

    /**
     * Finds placeholder by name.
     *
     * @param name Placeholder name.
     * @return Placeholder values.
     */
    @Nonnull
    public Optional<PlaceholderData> findPlaceholderByName(@Nonnull String name) {
        Validate.notNull(name, "name cannot be null!");

        for (PlaceholderData placeholderData : this.placeholders)
            if (placeholderData.getName().equals(name))
                return Optional.of(placeholderData);
        return Optional.empty();
    }

    /**
     * Finds placeholder by name.
     *
     * @param arg Argument.
     * @return Placeholder values.
     */
    @Nonnull
    public Optional<PlaceholderData> findPlaceholderByArg(@Nonnull String arg) {
        Validate.notNull(arg, "argument cannot be null!");
        String holder = CommandUtils.getPlaceholder(arg);
        return (holder == null) ? Optional.empty() : this.findPlaceholderByName(holder);
    }


    /*
    HANDLERS
     */

    /**
     * Registers command to server.
     */
    public void register() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            Command command = commandMap.getCommand(this.name);

            if (command != null && command.isRegistered())
                return;
            commandMap.register(this.name, this.listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters command from server.
     */
    public void unregister() {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");

            commandMap.setAccessible(true);
            knownCommands.setAccessible(true);

            ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(this.name);
            this.listener.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
