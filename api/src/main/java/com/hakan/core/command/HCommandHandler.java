package com.hakan.core.command;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.base.BaseCommandData;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.command.executors.sub.SubCommandData;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * HCommandHandler class to register
 * commands and sub commands to server.
 */
public final class HCommandHandler {

    /**
     * Registers commands to server.
     *
     * @param adapters List of command adapters.
     */
    public static void register(@Nonnull HCommandAdapter... adapters) {
        Validate.notNull(adapters, "adapters cannot be null!");

        for (HCommandAdapter adapter : adapters) {
            BaseCommand baseCommand = adapter.getClass().getAnnotation(BaseCommand.class);
            if (baseCommand == null)
                continue;

            BaseCommandData baseCommandData = new BaseCommandData(adapter, baseCommand);
            baseCommandData.register();

            for (Method method : adapter.getClass().getDeclaredMethods()) {
                SubCommand subCommand = method.getAnnotation(SubCommand.class);
                if (subCommand == null)
                    continue;

                SubCommandData subCommandData = new SubCommandData(method, subCommand);
                baseCommandData.addSubCommand(subCommandData);
            }
        }
    }
}