package com.hakan.core.message.bossbar;

import com.hakan.core.message.MessageHandler;
import com.hakan.core.message.bossbar.meta.BarColor;
import com.hakan.core.message.bossbar.meta.BarFlag;
import com.hakan.core.message.bossbar.meta.BarStyle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Boss bar class to show
 * boss bar to players and manage
 * the bossbar.
 */
public interface BossBar {

    /**
     * Gets title of bossbar.
     *
     * @return Title of bossbar.
     */
    @Nonnull
    String getTitle();

    /**
     * Sets title of bossbar.
     *
     * @param title Title of bossbar.
     */
    void setTitle(@Nonnull String title);

    /**
     * Gets color of bossbar.
     *
     * @return Color of bossbar.
     */
    @Nonnull
    BarColor getColor();

    /**
     * Sets color of bossbar.
     *
     * @param color Color of bossbar.
     */
    void setColor(@Nonnull BarColor color);

    /**
     * Gets style of bossbar.
     *
     * @return Style of bossbar.
     */
    @Nonnull
    BarStyle getStyle();

    /**
     * Sets style of bossbar.
     *
     * @param style Style of bossbar.
     */
    void setStyle(@Nonnull BarStyle style);

    /**
     * Checks if bossbar has flag.
     *
     * @param flag Bossbar flag.
     * @return If bossbar has flag, returns true.
     */
    boolean hasFlag(@Nonnull BarFlag flag);

    /**
     * Removes flag from bossbar.
     *
     * @param flag Bossbar flag.
     */
    void removeFlag(@Nonnull BarFlag flag);

    /**
     * Adds flag to bossbar.
     *
     * @param flag Bossbar flag.
     */
    void addFlag(@Nonnull BarFlag flag);

    /**
     * Gets progress of bossbar.
     *
     * @return Progress of bossbar.
     */
    double getProgress();

    /**
     * Sets progress of bossbar.
     *
     * @param progress Progress of bossbar.
     */
    void setProgress(double progress);

    /**
     * Gets player list who can see bossbar.
     *
     * @return Player list who can see bossbar.
     */
    @Nonnull
    List<Player> getPlayers();

    /**
     * Adds player to bossbar.
     *
     * @param player Player.
     */
    void addPlayer(@Nonnull Player player);

    /**
     * Removes player from bossbar.
     *
     * @param player Player.
     */
    void removePlayer(@Nonnull Player player);

    /**
     * Removes all players from bossbar.
     */
    void removeAll();

    /**
     * Checks bossbar is visible.
     *
     * @return If bossbar is visible, returns true.
     */
    boolean isVisible();

    /**
     * Sets visibility of bossbar.
     *
     * @param visible Visibility.
     */
    void setVisible(boolean visible);

    /**
     * Deletes bossbar.
     *
     * @return BossBar class.
     */
    default BossBar delete() {
        this.removeAll();
        MessageHandler.getBossBars().remove(this);
        return this;
    }
}
