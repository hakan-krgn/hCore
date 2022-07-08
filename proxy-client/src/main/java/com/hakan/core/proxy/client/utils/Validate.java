package com.hakan.core.proxy.client.utils;

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
    public static <T> T notNull(T object) {
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
    public static <T> T notNull(T object, String message) {
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
    public static void isTrue(boolean condition, String message) {
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
    public static void isFalse(boolean condition, String message) {
        Validate.isTrue(!condition, message);
    }
}