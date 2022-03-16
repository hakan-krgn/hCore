package com.hakan.core.worldborder.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.worldborder.HWorldBorderHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class HBorderPlayerConnectionListener extends HListenerAdapter {

    public HBorderPlayerConnectionListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HWorldBorderHandler.findByPlayer(player)
                .ifPresent(hWorldBorder -> hWorldBorder.hide(player));
    }
}