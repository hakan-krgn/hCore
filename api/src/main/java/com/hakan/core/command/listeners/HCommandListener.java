package com.hakan.core.command.listeners;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommandData;
import com.hakan.core.command.executors.sub.SubCommandData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public class HCommandListener extends BukkitCommand {

    private final BaseCommandData baseCommandData;

    /**
     * {@inheritDoc}
     */
    public HCommandListener(BaseCommandData baseCommandData) {
        super(baseCommandData.getName(),
                baseCommandData.getDescription(),
                baseCommandData.getUsage(),
                Arrays.asList(baseCommandData.getAliases()));
        this.baseCommandData = baseCommandData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        SubCommandData subCommandData = this.baseCommandData.findSubCommand(args).orElse(null);

        if (subCommandData == null) {
            sender.sendMessage(this.baseCommandData.getUsage());
            return false;
        } else if (!sender.hasPermission(subCommandData.getPermission())) {
            sender.sendMessage(subCommandData.getPermissionMessage());
            return false;
        }

        try {
            HCommandAdapter adapter = this.baseCommandData.getAdapter();
            Method method = subCommandData.getMethod();
            Class<?> parameterType = method.getParameterTypes()[0];

            if (parameterType.equals(Player.class) && sender instanceof Player) {
                Player player = (Player) sender;
                method.invoke(adapter, player, args);
            } else if (parameterType.equals(CommandSender.class)) {
                method.invoke(adapter, sender, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
        Set<String> tabComplete = new LinkedHashSet<>();

        List<String> enteredArgs = new LinkedList<>(Arrays.asList(args));
        List<SubCommandData> subCommandDatas = this.baseCommandData.getSubCommandsSafe();

        String lastArg = enteredArgs.get(enteredArgs.size() - 1);
        enteredArgs.remove(enteredArgs.size() - 1);

        int i = 0;
        for (; i < enteredArgs.size(); i++) {
            String arg = enteredArgs.get(i);
            for (SubCommandData subCommandData : new ArrayList<>(subCommandDatas)) {
                if (subCommandData.getArgs().length <= i || !arg.equals(subCommandData.getArgs()[i]))
                    subCommandDatas.remove(subCommandData);
            }
        }

        Set<String> tabCompleteBefore = new LinkedHashSet<>();
        for (SubCommandData subCommandData : subCommandDatas) {
            String[] subArgs = subCommandData.getArgs();
            int subArgLen = subArgs.length;
            if (subArgLen != 0 && subArgLen > i) {
                tabCompleteBefore.add(subArgs[i]);
            }
        }

        for (String tab : tabCompleteBefore) {
            if (tab.startsWith(lastArg)) {
                tabComplete.add(tab);
            }
        }

        tabComplete.addAll(tabCompleteBefore);

        return new ArrayList<>(tabComplete);
    }
}