package com.hakan.core.worldborder.listeners;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.worldborder.HWorldBorderHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class HBorderPlayerActionListener extends HListenerAdapter {

    public HBorderPlayerActionListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        HCore.syncScheduler().after(3).run(() -> {
            Player player = event.getPlayer();
            HWorldBorderHandler.findByPlayer(player)
                    .ifPresent(hWorldBorder -> hWorldBorder.show(player));
        });
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        HCore.syncScheduler().after(3).run(() -> {
            Player player = event.getPlayer();
            HWorldBorderHandler.findByPlayer(player)
                    .ifPresent(hWorldBorder -> hWorldBorder.show(player));
        });
    }
}