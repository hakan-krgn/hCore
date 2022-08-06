package com.hakan.core.hologram.line.item;

import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.GeneralUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Item line class.
 */
public interface ItemLine extends HologramLine {

    /**
     * Creates new item line.
     *
     * @param hologram  Hologram of line.
     * @param itemStack ItemStack to display.
     * @return Item line.
     */
    @Nonnull
    static ItemLine create(@Nonnull HHologram hologram, @Nonnull ItemStack itemStack) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(itemStack, "item stack cannot be null!");

        ItemLine line = GeneralUtils.createNewInstance("com.hakan.core.hologram.line.item.ItemLine_%s",
                new Class[]{HHologram.class, Location.class},
                new Object[]{hologram, hologram.getLocation()});
        line.setItem(itemStack);

        return line;
    }

    /**
     * Creates new item line.
     *
     * @param hologram  Hologram of line.
     * @param location  Location of line.
     * @param itemStack ItemStack to display.
     * @return Item line.
     */
    @Nonnull
    static ItemLine create(@Nonnull HHologram hologram, @Nonnull Location location, @Nonnull ItemStack itemStack) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(itemStack, "item stack cannot be null!");

        ItemLine line = ItemLine.create(hologram, itemStack);
        line.setLocation(location);

        return line;
    }


    /**
     * Gets item from line.
     *
     * @return Item of line.
     */
    @Nonnull
    ItemStack getItem();

    /**
     * Sets item of line.
     *
     * @param item item of line.
     */
    void setItem(@Nonnull ItemStack item);
}
