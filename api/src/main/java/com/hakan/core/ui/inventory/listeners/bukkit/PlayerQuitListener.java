package com.hakan.core.ui.inventory.listeners.bukkit;

import com.hakan.core.ui.inventory.HInventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * Player quit listener class.
 */
public final class PlayerQuitListener implements Listener {

    /**
     * Quit event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onQuit(@Nonnull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HInventoryHandler.findByPlayer(player)
                .ifPresent(hInventory -> hInventory.close(player));
    }
}