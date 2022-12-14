package com.hakan.core.hologram.line.item;

import com.hakan.core.hologram.Hologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.utils.ReflectionUtils;
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
    static ItemLine create(@Nonnull Hologram hologram,
                           @Nonnull ItemStack itemStack) {
        return ItemLine.create(hologram, hologram.getLocation(), itemStack);
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
    static ItemLine create(@Nonnull Hologram hologram,
                           @Nonnull Location location,
                           @Nonnull ItemStack itemStack) {
        Validate.notNull(hologram, "hologram cannot be null!");
        Validate.notNull(location, "location cannot be null!");
        Validate.notNull(itemStack, "item stack cannot be null!");

        ItemLine line = ReflectionUtils.newInstance("com.hakan.core.hologram.line.item.ItemLine_%s",
                new Class[]{Hologram.class, Location.class}, new Object[]{hologram, location});
        line.setLocation(location);
        line.setItem(itemStack);

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
