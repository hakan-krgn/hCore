package com.hakan.core.command;

import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.base.BaseCommandData;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.core.command.executors.sub.SubCommandData;

import java.lang.reflect.Method;

public class HCommandHandler {

    public static void register(HCommandAdapter... adapters) {
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