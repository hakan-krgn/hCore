package com.hakan.core.hologram.line.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Hologram armor stand interface.
 */
public interface HHologramArmorStand {

    /**
     * Gets text from line.
     *
     * @return Text of line.
     */
    @Nonnull
    String getText();

    /**
     * Sets text of line.
     *
     * @param text text of line.
     */
    void setText(@Nonnull String text);

    /**
     * Gets location of line.
     *
     * @return location of line.
     */
    @Nonnull
    Location getLocation();

    /**
     * Sets location of line.
     *
     * @param location location of line.
     */
    void setLocation(@Nonnull Location location);

    /**
     * Show line to players.
     *
     * @param players Players who can see line.
     */
    void show(@Nonnull List<Player> players);

    /**
     * Hide line to players.
     *
     * @param players Players who can't see line.
     */
    void hide(@Nonnull List<Player> players);
}