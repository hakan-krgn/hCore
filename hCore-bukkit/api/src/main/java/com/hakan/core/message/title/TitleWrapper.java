package com.hakan.core.message.title;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Title wrapper class.
 */
public interface TitleWrapper {

    /**
     * Sends title to player.
     *
     * @param player Player.
     * @param title  Title class.
     */
    void send(@Nonnull Player player, @Nonnull Title title);
}
