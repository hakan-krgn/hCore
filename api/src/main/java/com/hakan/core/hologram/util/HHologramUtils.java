package com.hakan.core.hologram.util;

import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

/**
 * Hologram util class
 */
@SuppressWarnings({"unchecked"})
public final class HHologramUtils {

    /**
     * Creates new instance of given class.
     *
     * @param path    Path of class.
     * @param classes Classes to be used in constructor.
     * @param objects Objects to be used in constructor.
     * @param <T>     Type of class.
     * @return New instance of HologramArmorStand class.
     */
    @Nonnull
    public static <T> T createNewInstance(@Nonnull String path, @Nonnull Class<?>[] classes, @Nonnull Object[] objects) {
        try {
            Validate.notNull(path, "path cannot be null!");
            Validate.notNull(classes, "classes cannot be null!");
            Validate.notNull(objects, "objects cannot be null!");

            Class<T> tClass = (Class<T>) Class.forName(path);
            Constructor<T> constructor = tClass.getDeclaredConstructor(classes);
            constructor.setAccessible(true);
            T instance = constructor.newInstance(objects);
            constructor.setAccessible(false);

            return instance;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}