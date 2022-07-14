package com.hakan.core.spam;

import com.hakan.core.HCore;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HSpam class to check
 * if the given text is spamming or not.
 */
public final class HSpam {

    private static final Map<String, Long> spams = new HashMap<>();

    /**
     * Gets difference between now
     * and end time of spam as millisecond.
     *
     * @param id The id to check.
     * @return Difference between now
     * and end time of spam as millisecond.
     */
    public static long remainTime(@Nonnull String id) {
        return HSpam.spams.get(id) - System.currentTimeMillis();
    }

    /**
     * Gets difference between now
     * and end time of spam as unit.
     *
     * @param id   The id to check.
     * @param unit The time unit.
     * @return Difference between now
     * and end time of spam as unit.
     */
    public static long remainTime(@Nonnull String id, @Nonnull TimeUnit unit) {
        return unit.convert(remainTime(id), TimeUnit.MILLISECONDS);
    }

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
        if (!HSpam.spams.containsKey(id)) {
            HSpam.spams.put(id, System.currentTimeMillis() + ticks * 50);
            HCore.syncScheduler().after(ticks)
                    .run(() -> HSpam.spams.remove(id));
            return false;
        }
        return true;
    }
}