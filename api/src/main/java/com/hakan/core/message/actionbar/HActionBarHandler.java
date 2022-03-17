package com.hakan.core.message.actionbar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Action bar handler class.
 */
public interface HActionBarHandler {

    /**
     * Sends action bar to player.
     *
     * @param player Player.
     * @param text   Text.
     */
    void send(@Nonnull Player player, @Nonnull String text);
}