package com.hakan.core.spam;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * HSpam class to check
 * if the given text is spamming or not.
 */
public final class HSpam {

    private static final Map<String, Long> spams = new HashMap<>();

    /**
     * Checks if id is spamming.
     *
     * @param id       The id to check.
     * @param duration The duration to check.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, @Nonnull Duration duration) {
        return HSpam.spam(id, duration.toMillis());
    }

    /**
     * Checks if id is spamming.
     *
     * @param id   The id to check.
     * @param time The time in milliseconds.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, long time) {
        if (spams.containsKey(id)) {
            if (spams.get(id) - System.currentTimeMillis() <= 0) {
                spams.remove(id);
                return false;
            }
        } else {
            spams.put(id, System.currentTimeMillis() + time);
        }
        return true;
    }
}