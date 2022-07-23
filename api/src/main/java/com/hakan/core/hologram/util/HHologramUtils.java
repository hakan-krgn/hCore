package com.hakan.core.hologram.util;

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
    public static <T> T createNewInstance(String path, Class<?>[] classes, Object[] objects) {
        try {
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