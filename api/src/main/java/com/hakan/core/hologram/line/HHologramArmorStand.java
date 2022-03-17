package com.hakan.core.hologram.line;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Vezor armor stand interface
 */
public interface HHologramArmorStand {

    /**
     * Gets armor stand id
     *
     * @return armor stand id
     */
    int getId();

    /**
     * Sets text of line
     *
     * @param text text of line
     */
    void setText(@Nonnull String text);

    /**
     * Gets text from line
     *
     * @return Text of line
     */
    String getText();

    /**
     * Sets location of line
     *
     * @param location location of line
     */
    void setLocation(@Nonnull Location location);

    /**
     * Gets location of line
     *
     * @return location of line
     */
    Location getLocation();

    /**
     * Show line to players
     *
     * @param players Players who can see line
     */
    void show(@Nonnull List<Player> players);

    /**
     * Hide line to players
     *
     * @param players Players who can't see line
     */
    void hide(@Nonnull List<Player> players);

}