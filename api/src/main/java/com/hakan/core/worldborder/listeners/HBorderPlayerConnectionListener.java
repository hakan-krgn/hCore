package com.hakan.core.worldborder.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.worldborder.HWorldBorderHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * Player connection listener.
 */
public final class HBorderPlayerConnectionListener extends HListenerAdapter {

    /**
     * Creates new instance of this class.
     *
     * @param plugin Main class of plugin.
     */
    public HBorderPlayerConnectionListener(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Player quit event.
     *
     * @param event Event.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(@Nonnull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HWorldBorderHandler.findByPlayer(player)
                .ifPresent(hWorldBorder -> hWorldBorder.hide(player));
    }
}