package com.hakan.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Listener adapter class.
 */
public abstract class HListenerAdapter implements Listener {

    /**
     * Registers listeners to bukkit.
     *
     * @param listeners Listeners.
     */
    public static void register(@Nonnull HListenerAdapter... listeners) {
        HListenerAdapter.register(Arrays.asList(listeners));
    }

    /**
     * Registers listeners to bukkit.
     *
     * @param listeners Listeners.
     */
    public static void register(@Nonnull Collection<HListenerAdapter> listeners) {
        Objects.requireNonNull(listeners, "listeners cannot be null!")
                .forEach(listenerAdapter -> Bukkit.getPluginManager().registerEvents(listenerAdapter, listenerAdapter.plugin));
    }


    protected final JavaPlugin plugin;

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin class.
     */
    public HListenerAdapter(@Nonnull JavaPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null!");
    }
}