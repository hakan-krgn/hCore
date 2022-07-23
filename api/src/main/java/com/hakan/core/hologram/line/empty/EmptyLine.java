package com.hakan.core.hologram.line.empty;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Empty line class.
 */
public final class EmptyLine implements HologramLine {

    /**
     * Creates new item line.
     *
     * @param hologram Hologram of line.
     * @return Item line.
     */
    @Nonnull
    public static EmptyLine create(@Nonnull HHologram hologram) {
        Validate.notNull(hologram, "hologram cannot be null!");
        return new EmptyLine(hologram, hologram.getLocation());
    }


    private final HHologram hologram;
    private Location location;

    /**
     * Creates new empty line.
     *
     * @param hologram Hologram of line.
     * @param location Location of line.
     */
    private EmptyLine(@Nonnull HHologram hologram, @Nonnull Location location) {
        this.hologram = hologram;
        this.location = location;
    }

    /**
     * Gets parent class of line.
     *
     * @return Parent class of line.
     */
    @Nonnull
    public HHologram getHologram() {
        return this.hologram;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return this.location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        this.location = location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {

    }
}
