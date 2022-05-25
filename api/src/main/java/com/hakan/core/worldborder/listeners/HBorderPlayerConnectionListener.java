package com.hakan.core.worldborder.listeners;

import com.hakan.core.worldborder.HWorldBorderHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

/**
 * Player connection listener.
 */
public final class HBorderPlayerConnectionListener implements Listener {

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