package com.hakan.core.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Validate class to validate objects.
 */
public final class Validate {

    /**
     * Checks if the object is null.
     *
     * @param object The object to check.
     * @return The object.
     * @throws NullPointerException If the object is null.
     */
    @Nonnull
    public static <T> T notNull(@Nullable T object) {
        if (object == null)
            throw new NullPointerException();
        return object;
    }

    /**
     * Checks if the object is null.
     *
     * @param object  The object to check.
     * @param message The message to throw.
     * @param <T>     The type of the object.
     * @return The object.
     * @throws NullPointerException If the object is null.
     */
    @Nonnull
    public static <T> T notNull(@Nullable T object, @Nonnull String message) {
        if (object == null)
            throw new NullPointerException(message);
        return object;
    }

    /**
     * Checks if the condition is true.
     *
     * @param condition The condition to check.
     * @throws IllegalArgumentException If the object is null.
     */
    public static void isTrue(boolean condition) {
        if (condition)
            throw new IllegalArgumentException();
    }

    /**
     * Checks if the condition is true.
     *
     * @param condition The condition to check.
     * @param message   The message to throw.
     * @throws IllegalArgumentException If the object is null.
     */
    public static void isTrue(boolean condition, @Nonnull String message) {
        if (condition)
            throw new IllegalArgumentException(message);
    }

    /**
     * Checks if the condition is false.
     *
     * @param condition The condition to check.
     * @throws IllegalArgumentException If the object is null.
     */
    public static void isFalse(boolean condition) {
        Validate.isTrue(!condition);
    }

    /**
     * Checks if the condition is false.
     *
     * @param condition The condition to check.
     * @param message   The message to throw.
     * @throws IllegalArgumentException If the object is null.
     */
    public static void isFalse(boolean condition, @Nonnull String message) {
        Validate.isTrue(!condition, message);
    }
}
