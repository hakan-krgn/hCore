package com.hakan.core.command.listeners;

import com.hakan.core.command.executors.basecommand.BaseCommandData;
import com.hakan.core.command.executors.placeholder.PlaceholderData;
import com.hakan.core.command.executors.subcommand.SubCommandData;
import com.hakan.core.command.utils.CommandUtils;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public final class CommandListener extends Command implements TabExecutor {

    private final BaseCommandData baseCommandData;

    /**
     * {@inheritDoc}
     */
    public CommandListener(@Nonnull BaseCommandData baseCommandData) {
        super(baseCommandData.getName(),
                baseCommandData.getDescription(),
                baseCommandData.getAliases());
        this.baseCommandData = Validate.notNull(baseCommandData, "base command data cannot be null!");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SubCommandData subCommandData = this.baseCommandData.findSubCommand(args).orElse(null);

        if (subCommandData == null) {
            sender.sendMessage(this.baseCommandData.getUsage());
            return;
        } else if (!subCommandData.getPermission().isEmpty()) {
            if (!CommandUtils.hasPermission(sender, subCommandData.getPermission())) {
                sender.sendMessage(subCommandData.getPermissionMessage());
                return;
            }
        }


        Method method = subCommandData.getMethod();
        Class<?>[] parameters = method.getParameterTypes();

        if (parameters[0].equals(ProxiedPlayer.class) && sender instanceof ProxiedPlayer) {
            ReflectionUtils.invoke(this.baseCommandData.getAdapter(), method, sender, args);
        } else if (parameters[0].equals(CommandSender.class)) {
            ReflectionUtils.invoke(this.baseCommandData.getAdapter(), method, sender, args);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!this.baseCommandData.isTabComplete())
            return new ArrayList<>();


        String lastArg = args[args.length - 1];
        Set<String> tabComplete = new LinkedHashSet<>();
        Set<String> tabCompleteBefore = new LinkedHashSet<>();
        List<String> enteredArgs = new LinkedList<>(Arrays.asList(args));
        List<SubCommandData> subCommandDatas = this.baseCommandData.getSubCommandsSafe();

        enteredArgs.remove(enteredArgs.size() - 1);

        int i = 0;
        for (; i < enteredArgs.size(); i++) {
            String arg = enteredArgs.get(i);
            for (SubCommandData subCommandData : new ArrayList<>(subCommandDatas)) {
                String[] subArgs = subCommandData.getArgs();
                if (subArgs.length <= i || !(subArgs[i].equals(arg) || CommandUtils.hasPlaceholder(subArgs[i])))
                    subCommandDatas.remove(subCommandData);
            }
        }

        for (SubCommandData subCommandData : subCommandDatas) {
            String[] subArgs = subCommandData.getArgs();
            int subArgLen = subArgs.length;
            if (subArgLen != 0 && subArgLen > i) {
                tabCompleteBefore.add(subArgs[i]);
            }
        }

        for (String tab : new HashSet<>(tabCompleteBefore)) {
            PlaceholderData placeholder = this.baseCommandData.findPlaceholderByArg(tab).orElse(null);
            if (placeholder != null) {
                if (CommandUtils.hasPermission(sender, placeholder.getPermission()))
                    tabComplete.addAll(placeholder.getValues());
                tabCompleteBefore.remove(tab);
            } else {
                tabComplete.add(tab);
            }
        }

        tabComplete.addAll(tabCompleteBefore);


        Set<String> tabCompleteList = new LinkedHashSet<>();
        tabComplete.stream().filter(tab -> tab.equals(lastArg)).forEach(tabCompleteList::add);
        tabComplete.stream().filter(tab -> tab.startsWith(lastArg)).forEach(tabCompleteList::add);
        tabCompleteList.addAll(tabComplete);

        return new ArrayList<>(tabCompleteList);
    }
}
