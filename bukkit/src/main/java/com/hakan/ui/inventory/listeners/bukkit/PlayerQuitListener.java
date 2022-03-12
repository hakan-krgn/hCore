package com.hakan.ui.inventory.listeners.bukkit;

import com.hakan.listener.HListenerAdapter;
import com.hakan.ui.inventory.HInventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class PlayerQuitListener extends HListenerAdapter {

    public PlayerQuitListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HInventoryHandler.findByPlayer(player)
                .ifPresent(hInventory -> hInventory.close(player));
    }
}