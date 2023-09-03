package com.hakan.core.hologram.line;

import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.line.empty.EmptyLine;
import com.hakan.core.hologram.line.item.ItemLine;
import com.hakan.core.hologram.line.text.TextLine;
import com.hakan.core.utils.Validate;
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
     * @param object   Object.
     * @return Line.
     */
    @Nonnull
    static HologramLine create(@Nonnull Hologram hologram,
                               @Nullable Object object) {
        if (object == null) {
            return EmptyLine.create(hologram);
        } else if (object instanceof ItemStack) {
            ItemStack item = (ItemStack) object;
            return ItemLine.create(hologram, item);
        } else if (object instanceof String) {
            String text = (String) object;
            return (text.isEmpty()) ? EmptyLine.create(hologram) : TextLine.create(hologram, text);
        }
        throw new IllegalArgumentException("value must be String, ItemStack or null!");
    }

    /**
     * Creates new hologram line.
     *
     * @param hologram Hologram of line.
     * @param location Location of line.
     * @param object   Object.
     * @return Line.
     */
    @Nonnull
    static HologramLine create(@Nonnull Hologram hologram,
                               @Nonnull Location location,
                               @Nullable Object object) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(location, "location cannot be null!");

        HologramLine line = HologramLine.create(hologram, object);
        line.setLocation(location);

        return line;
    }


    /**
     * Gets parent class of line.
     *
     * @return Parent class of line.
     */
    @Nonnull
    Hologram getHologram();

    /**
     * Gets entity id of line.
     *
     * @return Entity id of line.
     */
    int getEntityID();

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
     * Sets marker of line.
     *
     * @param marker Marker of line.
     */
    void setMarker(boolean marker);

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
