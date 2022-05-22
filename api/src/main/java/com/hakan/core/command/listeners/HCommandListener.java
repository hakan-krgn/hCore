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
import java.util.List;

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
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
    }
}