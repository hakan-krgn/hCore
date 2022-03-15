package com.hakan.core.packet.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.packet.HPacketHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class PlayerConnectionListener extends HListenerAdapter {

    public PlayerConnectionListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        HPacketHandler.create(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        HPacketHandler.create(event.getPlayer());
    }
}