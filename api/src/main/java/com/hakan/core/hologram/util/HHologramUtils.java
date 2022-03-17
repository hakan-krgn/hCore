package com.hakan.core.hologram.util;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.entity.HHologramArmorStand;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

/**
 * Hologram util class
 */
public final class HHologramUtils {

    /**
     * Creates new instance of VezorArmorStand class
     *
     * @param hHologram instance of hologram
     * @return new instance of VezorArmorStand class
     */
    @Nonnull
    public static HHologramArmorStand createVezorArmorStand(@Nonnull HHologram hHologram) {
        try {
            Validate.notNull(hHologram, "vezorHologram cannot be null");

            Constructor<?> constructor = Class.forName("com.hakan.core.line.entity.HHologramArmorStand_" + HCore.getVersionString()).getDeclaredConstructor(HHologram.class, Location.class);
            constructor.setAccessible(true);
            HHologramArmorStand vezorHologramArmorStand = (HHologramArmorStand) constructor.newInstance(hHologram, hHologram.getLocation());
            constructor.setAccessible(false);

            return vezorHologramArmorStand;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}