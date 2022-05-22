package com.hakan.core.command.executors.base;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.sub.SubCommandData;
import com.hakan.core.command.listeners.HCommandListener;
import com.hakan.core.command.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"unchecked"})
public class BaseCommandData {

    private final HCommandAdapter adapter;

    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;
    private final HCommandListener listener;
    private final List<SubCommandData> subCommands;

    public BaseCommandData(HCommandAdapter adapter, BaseCommand baseCommand) {
        this.adapter = adapter;
        this.name = baseCommand.name();
        this.usage = baseCommand.usage();
        this.aliases = baseCommand.aliases();
        this.description = baseCommand.description();

        this.subCommands = new ArrayList<>();
        this.listener = new HCommandListener(this);
    }

    public HCommandAdapter getAdapter() {
        return this.adapter;
    }

    public String getName() {
        return this.name;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public void addSubCommand(SubCommandData subCommand) {
        this.subCommands.add(subCommand);
    }

    public void removeSubCommand(SubCommandData subCommand) {
        this.subCommands.remove(subCommand);
    }

    public Optional<SubCommandData> findSubCommand(String[] subCommands) {
        for (SubCommandData subCommandData : this.subCommands) {
            String[] commands = subCommandData.getArgs();
            if (CommandUtils.isMatching(subCommands, commands)) {
                return Optional.of(subCommandData);
            }
        }
        return Optional.empty();
    }


    /*
    HANDLERS
     */
    public void register() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            Command command = commandMap.getCommand(this.name);

            if (command != null && command.isRegistered())
                throw new IllegalStateException("This command already registered.");
            commandMap.register(this.name, this.listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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