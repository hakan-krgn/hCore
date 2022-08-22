package com.hakan.core.worldborder.border;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * HWorldBorder class to
 * manage world border.
 */
public interface HWorldBorder {

    /**
     * Shows world border to player.
     *
     * @param player Player.
     */
    void show(@Nonnull Player player);

    /**
     * Shows world border to everyone.
     */
    void showAll();

    /**
     * Hides world border from player.
     *
     * @param player Player.
     */
    void hide(@Nonnull Player player);

    /**
     * Hides world border from everyone.
     */
    void hideAll();

    /**
     * Deletes world border.
     */
    void delete();

    /**
     * Gets shown viewers.
     *
     * @return Shown viewers.
     */
    @Nonnull
    Set<Player> getShownViewers();

    /**
     * Gets center location.
     *
     * @return Center location.
     */
    @Nonnull
    Location getCenter();

    /**
     * Sets center location.
     *
     * @param location Center location.
     */
    void setCenter(@Nonnull Location location);

    /**
     * Gets color.
     *
     * @return Color.
     */
    @Nonnull
    HBorderColor getColor();

    /**
     * Sets color.
     *
     * @param hBorderColor Color.
     */
    void setColor(@Nonnull HBorderColor hBorderColor);

    /**
     * Gets size.
     *
     * @return Size.
     */
    double getSize();

    /**
     * Sets size.
     *
     * @param size Size.
     */
    void setSize(double size);

    /**
     * Changes size of worldborder
     * in given time.
     *
     * @param size Size.
     * @param time Time.
     */
    void setSize(double size, long time);

    /**
     * Gets damage amount.
     *
     * @return Damage amount.
     */
    double getDamageAmount();

    /**
     * Sets damage amount.
     *
     * @param damageAmount Damage amount.
     */
    void setDamageAmount(double damageAmount);

    /**
     * Gets damage buffer.
     *
     * @return Damage buffer.
     */
    double getDamageBuffer();

    /**
     * Sets damage buffer.
     *
     * @param damageBuffer Damage buffer.
     */
    void setDamageBuffer(double damageBuffer);

    /**
     * Gets warning distance.
     *
     * @return Warning distance.
     */
    int getWarningDistance();

    /**
     * Sets warning distance.
     *
     * @param warningDistance Warning distance.
     */
    void setWarningDistance(int warningDistance);

    /**
     * Gets warning time.
     *
     * @return Warning time.
     */
    int getWarningTime();

    /**
     * Sets warning time.
     *
     * @param warningTime Warning time.
     */
    void setWarningTime(int warningTime);

    /**
     * Updates all values of world border
     * and shows the viewers.
     */
    void update();
}