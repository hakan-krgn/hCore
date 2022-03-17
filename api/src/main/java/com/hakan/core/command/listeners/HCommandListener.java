package com.hakan.core.command.listeners;

import com.hakan.core.command.HCommandExecutor;
import com.hakan.core.command.HSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * Listener class for command executor.
 */
public final class HCommandListener extends BukkitCommand {

    private final HCommandExecutor executor;

    /**
     * Creates new instance of listener.
     *
     * @param executor Command executor.
     */
    public HCommandListener(@Nonnull HCommandExecutor executor) {
        super(Objects.requireNonNull(executor).getCommand());
        super.setAliases(Arrays.asList(executor.getAliases()));
        super.setPermission(executor.getPermission());

        this.executor = executor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        for (Function<HCommandExecutor, Boolean> function : this.executor.getFilters())
            if (!function.apply(this.executor))
                return false;

        this.executor.onCommand(sender, args);
        return false;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Registers to bukkit map.
     */
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

    /**
     * Unregisters from bukkit map.
     */
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