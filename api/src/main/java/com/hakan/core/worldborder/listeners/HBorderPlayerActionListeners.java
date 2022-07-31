package com.hakan.core.worldborder.listeners;

import com.hakan.core.worldborder.HWorldBorderHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Border action listeners class.
 */
public final class HBorderPlayerActionListeners implements Listener {

    /**
     * Teleport event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onTeleport(@Nonnull PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();

        if (to == null || to.getWorld() == null || from.getWorld() == null)
            return;
        else if (!Objects.equals(from.getWorld(), to.getWorld()))
            return;

        HWorldBorderHandler.findByPlayer(player)
                .ifPresent(hWorldBorder -> hWorldBorder.show(player));
    }

    /**
     * World change event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onWorldChange(@Nonnull PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        HWorldBorderHandler.findByPlayer(player)
                .ifPresent(hWorldBorder -> hWorldBorder.hide(player));
    }
}