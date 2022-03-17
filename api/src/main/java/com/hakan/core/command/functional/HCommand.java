package com.hakan.core.command.functional;

import com.hakan.core.command.HCommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * HCommand class. This class is
 * available for use with lambda.
 */
public final class HCommand extends HCommandExecutor {

    private Consumer<CommandData> consumer;

    /**
     * Creates new instance of this class.
     *
     * @param command Command name.
     * @param aliases Aliases of command.
     */
    public HCommand(@Nonnull String command, @Nonnull String... aliases) {
        super(command, null, aliases);
    }

    /**
     * When command triggers, this consumer
     * will run.
     *
     * @param consumer Command consumer.
     */
    public HCommand onCommand(@Nonnull Consumer<CommandData> consumer) {
        this.consumer = Objects.requireNonNull(consumer, "consumer cannot be null!");
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCommand(@Nonnull CommandSender sender, @Nonnull String... args) {
        CommandData commandData = new CommandData(sender, this.getCommand(), args);
        if (this.consumer != null)
            this.consumer.accept(commandData);
    }
}