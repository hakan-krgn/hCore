package com.hakan.core.border.listeners;

import com.hakan.core.border.BorderHandler;
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
public final class BorderActionListeners implements Listener {

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

        BorderHandler.findByPlayer(player)
                .ifPresent(border -> border.show(player));
    }

    /**
     * World change event.
     *
     * @param event Event.
     */
    @EventHandler
    public void onWorldChange(@Nonnull PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        BorderHandler.findByPlayer(player)
                .ifPresent(border -> border.hide(player));
    }
}