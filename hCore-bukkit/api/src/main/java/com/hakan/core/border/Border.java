package com.hakan.core.border;

import com.hakan.core.border.color.BorderColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Border class to
 * manage world border.
 */
public interface Border {

    /**
     * Gets shown viewers.
     *
     * @return Shown viewers.
     */
    @Nonnull
    Player getViewer();

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
    BorderColor getColor();

    /**
     * Sets color.
     *
     * @param borderColor Color.
     */
    void setColor(@Nonnull BorderColor borderColor);

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
    default void setSize(double size) {
        this.setSize(size, 0);
    }

    /**
     * Changes size of worldborder
     * in given time.
     *
     * @param size New size of border.
     * @param time Time as millisecond.
     */
    void setSize(double size, long time);

    /**
     * Updates all values of world border
     * and shows the latest version of
     * world border to viewer.
     */
    void update();

    /**
     * Deletes world border from viewer
     * and remove it from border map in
     * BorderHandler class.
     */
    void delete();
}
