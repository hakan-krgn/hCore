package com.hakan.core.hologram.line;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.empty.EmptyLine;
import com.hakan.core.hologram.line.item.ItemLine;
import com.hakan.core.hologram.line.text.TextLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Hologram line class.
 */
@SuppressWarnings("unused")
public interface HologramLine {

    /**
     * Creates new hologram line.
     *
     * @param hologram Hologram of line.
     * @param t        Object.
     * @param <T>      Object type.
     * @return Line.
     */
    static <T> HologramLine create(@Nonnull HHologram hologram, @Nullable T t) {
        if (t == null)
            return EmptyLine.create(hologram);
        else if (t instanceof String)
            return TextLine.create(hologram, (String) t);
        else if (t instanceof ItemStack)
            return ItemLine.create(hologram, (ItemStack) t);
        throw new IllegalArgumentException("value must be text, item stack or null!");
    }


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