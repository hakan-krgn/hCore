package com.hakan.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"unchecked"})
public abstract class HCommandExecutor extends HSubCommand {

    private final String permission;
    private final String[] aliases;
    private final HCommandListener commandListener;
    final List<Function<HCommandExecutor, Boolean>> functions;

    public HCommandExecutor(String command, String permission, String... aliases) {
        super(command);
        this.permission = permission;
        this.aliases = aliases;
        this.functions = new ArrayList<>();
        this.commandListener = new HCommandListener(this);
    }

    public final String getCommand() {
        return super.getName();
    }

    public final String getPermission() {
        return this.permission;
    }

    public final String[] getAliases() {
        return this.aliases;
    }

    public final <T extends HCommandExecutor> T filter(Function<HCommandExecutor, Boolean> function) {
        this.functions.add(function);
        return (T) this;
    }

    public final <T extends HCommandExecutor> T register() {
        this.commandListener.register();
        return (T) this;
    }

    public final <T extends HCommandExecutor> T unregister() {
        this.commandListener.unregister();
        return (T) this;
    }


    protected abstract void onCommand(CommandSender sender, String... args);
}