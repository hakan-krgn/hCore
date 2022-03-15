package com.hakan.core.ui.inventory.listeners.bukkit;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.ui.inventory.HInventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class PluginDisableListener extends HListenerAdapter {

    public PluginDisableListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onQuit(PluginDisableEvent event) {
        if (!event.getPlugin().equals(this.plugin))
            return;

        Bukkit.getOnlinePlayers().forEach(player -> HInventoryHandler.findByPlayer(player)
                .ifPresent(hInventory -> hInventory.close(player)));
    }
}