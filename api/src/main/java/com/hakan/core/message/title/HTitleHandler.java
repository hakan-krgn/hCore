package com.hakan.core.message.title;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * HTitle handler class.
 */
public interface HTitleHandler {

    /**
     * Sends title to player.
     *
     * @param player Player.
     * @param hTitle HTitle class.
     */
    void send(@Nonnull Player player, @Nonnull HTitle hTitle);
}