package com.hakan.core.hologram.util;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.line.entity.HHologramArmorStand;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignHandler;
import com.hakan.core.ui.sign.listeners.HSignListener;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * Hologram util class
 */
public final class HHologramUtils {

    public static Class<?> HOLOGRAM_CLASS;

    public static void initialize() {
        try {

            Class<?> clazz = Class.forName("com.hakan.core.hologram.line.entity.HHologramArmorStand_" + HCore.getVersionString());
            if (HSign.class.isAssignableFrom(clazz)) {
                HHologramUtils.HOLOGRAM_CLASS = clazz;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not initialize hologram system. Probably you are using an unsupported version. (" + HCore.getVersionString() + ")");
        }
    }

    /**
     * Creates new instance of HologramArmorStand class.
     *
     * @param hHologram instance of hologram
     * @return New instance of HologramArmorStand class.
     */
    @Nonnull
    public static HHologramArmorStand createHologramArmorStand(@Nonnull HHologram hHologram) {
        try {
            Objects.requireNonNull(hHologram, "hHologram cannot be null");

            Constructor<?> constructor = HHologramUtils.HOLOGRAM_CLASS.getDeclaredConstructor(HHologram.class, Location.class);
            constructor.setAccessible(true);
            HHologramArmorStand armorStand = (HHologramArmorStand) constructor.newInstance(hHologram, hHologram.getLocation());
            constructor.setAccessible(false);

            return armorStand;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

}