package com.hakan.core.command;

import com.hakan.core.command.executors.basecommand.BaseCommand;
import com.hakan.core.command.executors.basecommand.BaseCommandData;
import com.hakan.core.command.executors.placeholder.Placeholder;
import com.hakan.core.command.executors.placeholder.PlaceholderData;
import com.hakan.core.command.executors.subcommand.SubCommand;
import com.hakan.core.command.executors.subcommand.SubCommandData;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * HCommandHandler class to register
 * commands and sub commands to server.
 */
public final class CommandHandler {

    /**
     * Registers commands to server.
     *
     * @param adapters List of com.hakan.core.command adapters.
     */
    public static void register(@Nonnull Object... adapters) {
        Validate.notNull(adapters, "adapters cannot be null!");

        for (Object adapter : adapters) {
            BaseCommand baseCommand = adapter.getClass().getAnnotation(BaseCommand.class);
            if (baseCommand == null)
                continue;

            BaseCommandData baseCommandData = new BaseCommandData(adapter, baseCommand);
            baseCommandData.register();

            for (Method method : adapter.getClass().getDeclaredMethods()) {
                SubCommand subCommand = method.getAnnotation(SubCommand.class);
                if (subCommand != null) {
                    SubCommandData subCommandData = new SubCommandData(baseCommandData, method, subCommand);
                    baseCommandData.addSubCommand(subCommandData);
                }

                Placeholder placeholder = method.getAnnotation(Placeholder.class);
                if (placeholder != null) {
                    PlaceholderData placeholderData = new PlaceholderData(baseCommandData, method, placeholder);
                    baseCommandData.addPlaceholder(placeholderData);
                }
            }
        }
    }
}
