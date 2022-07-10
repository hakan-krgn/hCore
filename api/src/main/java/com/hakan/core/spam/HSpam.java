package com.hakan.core.spam;

import com.hakan.core.HCore;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * HSpam class to check
 * if the given text is spamming or not.
 */
public final class HSpam {

    private static final Set<String> spams = new HashSet<>();

    /**
     * Checks if id is spamming.
     *
     * @param id   The id to check.
     * @param time Time.
     * @param unit Time unit.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, int time, @Nonnull TimeUnit unit) {
        return HSpam.spam(id, unit.toMillis(time) / 50);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id       The id to check.
     * @param duration The duration to check.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, @Nonnull Duration duration) {
        return HSpam.spam(id, duration.toMillis() / 50);
    }

    /**
     * Checks if id is spamming.
     *
     * @param id    The id to check.
     * @param ticks The time in ticks.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, long ticks) {
        if (!HSpam.spams.contains(id)) {
            HSpam.spams.add(id);
            HCore.syncScheduler().after(ticks)
                    .run(() -> HSpam.spams.remove(id));
            return false;
        }
        return true;
    }
}