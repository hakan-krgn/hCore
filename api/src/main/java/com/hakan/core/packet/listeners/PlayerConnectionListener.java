package com.hakan.core.packet.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.packet.HPacketHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * Player connection listeners to
 * handle and listen the packets.
 */
public final class PlayerConnectionListener extends HListenerAdapter {

    /**
     * Creates new instance of this class
     *
     * @param plugin Main class.
     */
    public PlayerConnectionListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Join listener handler.
     *
     * @param event Event.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(@Nonnull PlayerJoinEvent event) {
        HPacketHandler.register(event.getPlayer());
    }

    /**
     * Quit listener handler.
     *
     * @param event Event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(@Nonnull PlayerQuitEvent event) {
        HPacketHandler.unregister(event.getPlayer());
    }
}