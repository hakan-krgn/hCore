package com.hakan.core.hologram.line.item;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.HologramLine;
import com.hakan.core.hologram.util.HHologramUtils;
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

        String path = "com.hakan.core.hologram.line.item.ItemLine_" + HCore.getVersionString();
        Class<?>[] classes = new Class[]{HHologram.class, Location.class};
        Object[] objects = new Object[]{hologram, hologram.getLocation()};

        ItemLine line = HHologramUtils.createNewInstance(path, classes, objects);
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
