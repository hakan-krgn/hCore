package com.hakan.core.command;

import com.hakan.core.command.listeners.HCommandListener;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Executor class.
 * This class should extend with
 * any command class.
 */
@SuppressWarnings({"unchecked"})
public abstract class HCommandExecutor extends HSubCommand {

    private final String permission;
    private final String[] aliases;
    private final HCommandListener commandListener;
    private final List<Function<HCommandExecutor, Boolean>> functions;

    /**
     * Creates new instance of this class.
     *
     * @param command    Command name.
     * @param permission Permission.
     * @param aliases    Aliases of command.
     */
    public HCommandExecutor(@Nonnull String command, @Nullable String permission, @Nonnull String... aliases) {
        super(command);
        this.permission = permission;
        this.aliases = Objects.requireNonNull(aliases, "aliases cannot be null!");
        this.functions = new ArrayList<>();
        this.commandListener = new HCommandListener(this);
    }

    /**
     * Gets command name.
     *
     * @return Command name.
     */
    @Nonnull
    public final String getCommand() {
        return super.getName();
    }

    /**
     * Gets permission.
     *
     * @return Permission.
     */
    @Nullable
    public final String getPermission() {
        return this.permission;
    }

    /**
     * Gets aliases of command.
     *
     * @return Aliases of command.
     */
    @Nonnull
    public final String[] getAliases() {
        return this.aliases;
    }

    /**
     * Gets filters.
     *
     * @return Filters.
     */
    @Nonnull
    public List<Function<HCommandExecutor, Boolean>> getFilters() {
        return this.functions;
    }

    /**
     * Filtering system to add conditions to
     * command triggering.
     *
     * @param function Function.
     * @return This class.
     */
    @Nonnull
    public final <T extends HCommandExecutor> T filter(@Nonnull Function<HCommandExecutor, Boolean> function) {
        this.functions.add(Objects.requireNonNull(function, "function cannot be null!"));
        return (T) this;
    }

    /**
     * Registers command to listener
     * and bukkit command map.
     *
     * @return This class.
     */
    @Nonnull
    public final <T extends HCommandExecutor> T register() {
        this.commandListener.register();
        return (T) this;
    }

    /**
     * Unregisters command from listener
     * and bukkit command map.
     *
     * @return This class.
     */
    @Nonnull
    public final <T extends HCommandExecutor> T unregister() {
        this.commandListener.unregister();
        return (T) this;
    }


    /**
     * This method will trigger when
     * command runs.
     *
     * @param sender Command sender.
     * @param args   Arguments of command.
     */
    public abstract void onCommand(@Nonnull CommandSender sender, @Nonnull String... args);
}