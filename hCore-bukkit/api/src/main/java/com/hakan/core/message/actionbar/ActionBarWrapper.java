package com.hakan.core.message.actionbar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Action bar wrapper class.
 */
public interface ActionBarWrapper {

    /**
     * Sends action bar to player.
     *
     * @param player Player.
     * @param text   Text.
     */
    void send(@Nonnull Player player, @Nonnull String text);
}
