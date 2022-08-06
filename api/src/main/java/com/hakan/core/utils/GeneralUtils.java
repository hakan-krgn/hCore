package com.hakan.core.utils;

import com.hakan.core.HCore;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

/**
 * General utils class for hCore.
 */
@SuppressWarnings({"unchecked"})
public final class GeneralUtils {

    /**
     * Creates new instance of the given class.
     *
     * @param path Path of the class.
     * @param <T>  Type.
     * @return New instance of created class.
     */
    @Nonnull
    public static <T> T createNewInstance(@Nonnull String path) {
        return createNewInstance(path, new Class[0], new Object[0]);
    }

    /**
     * Creates new instance of given class.
     *
     * @param path    Path of class.
     * @param classes Classes to be used in constructor.
     * @param objects Objects to be used in constructor.
     * @param <T>     Type of class.
     * @return New instance of created class.
     */
    @Nonnull
    public static <T> T createNewInstance(@Nonnull String path, @Nonnull Class<?>[] classes, @Nonnull Object[] objects) {
        try {
            Validate.notNull(path, "path cannot be null!");
            Validate.notNull(classes, "classes cannot be null!");
            Validate.notNull(objects, "objects cannot be null!");

            Class<T> tClass = null;
            for (String version : HCore.getProtocolVersion().getKeys()) {
                try {
                    tClass = (Class<T>) Class.forName(path.replace("%s", version));
                    break;
                } catch (Exception ignored) {

                }
            }

            if (tClass == null)
                throw new NullPointerException("could not find class for path (" + path + ")");

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