package com.hakan.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

public abstract class HListenerAdapter implements Listener {

    public static void register(@Nonnull HListenerAdapter... listeners) {
        for (HListenerAdapter listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, listener.plugin);
        }
    }

    public static void register(@Nonnull Collection<HListenerAdapter> listeners) {
        HListenerAdapter.register(listeners.toArray(new HListenerAdapter[0]));
    }


    protected final JavaPlugin plugin;

    public HListenerAdapter(@Nonnull JavaPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null!");
    }
}