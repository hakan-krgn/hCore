package com.hakan.core.hologram.util;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.entity.HHologramArmorStand;
import com.hakan.core.utils.Validate;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

/**
 * Hologram util class
 */
public final class HHologramUtils {

    /**
     * Creates new instance of HologramArmorStand class.
     *
     * @param hHologram instance of hologram
     * @return New instance of HologramArmorStand class.
     */
    @Nonnull
    public static HHologramArmorStand createHologramArmorStand(@Nonnull HHologram hHologram) {
        try {
            Validate.notNull(hHologram, "hHologram cannot be null");

            Class<?> clazz = Class.forName("com.hakan.core.hologram.line.entity.HHologramArmorStand_" + HCore.getVersionString());
            Constructor<?> constructor = clazz.getDeclaredConstructor(HHologram.class, Location.class);
            constructor.setAccessible(true);
            HHologramArmorStand armorStand = (HHologramArmorStand) constructor.newInstance(hHologram, hHologram.getLocation());
            constructor.setAccessible(false);

            return armorStand;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}