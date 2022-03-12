package com.hakan.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class HCommandListener extends BukkitCommand {

    private final HCommandExecutor executor;

    HCommandListener(HCommandExecutor executor) {
        super(executor.getCommand());
        super.setAliases(Arrays.asList(executor.getAliases()));
        super.setPermission(executor.getPermission());

        this.executor = executor;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        for (Function<HCommandExecutor, Boolean> function : this.executor.functions)
            if (!function.apply(this.executor))
                return false;

        this.executor.onCommand(sender, args);
        return false;
    }

    @Override
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
        HSubCommand subCommand = this.executor;
        for (String arg : args) {
            HSubCommand hSubCommand = subCommand.findSubCommand(arg).orElse(null);
            if (hSubCommand != null) subCommand = hSubCommand;
        }

        if (subCommand.getSubCommands().isEmpty()) {
            List<String> list = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers())
                list.add(player.getName());
            return list;
        }

        return new ArrayList<>(subCommand.getSubCommands().keySet());
    }

    public void register() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            Command command = commandMap.getCommand(this.getName());

            if (command == null || !command.isRegistered()) {
                commandMap.register(this.getName(), this);
            } else {
                throw new IllegalStateException("This command already registered.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void unregister() {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");

            commandMap.setAccessible(true);
            knownCommands.setAccessible(true);

            ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(this.getName());
            this.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}