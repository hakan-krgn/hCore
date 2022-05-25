package com.hakan.core.ui.inventory.listeners.bukkit;

import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.HInventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import javax.annotation.Nonnull;

/**
 * Plugin disable listener class.
 */
public final class PluginDisableListener implements Listener {

    /**
     * Plugin disable event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onQuit(@Nonnull PluginDisableEvent event) {
        if (!event.getPlugin().equals(HCore.getInstance()))
            return;

        Bukkit.getOnlinePlayers().forEach(player -> HInventoryHandler.findByPlayer(player)
                .ifPresent(hInventory -> hInventory.close(player)));
    }
}