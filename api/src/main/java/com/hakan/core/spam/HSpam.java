package com.hakan.core.spam;

import com.hakan.core.HCore;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HSpam class to check
 * if the given text is spamming or not.
 */
public class HSpam {

    private static final Map<String, Long> spamMap = new HashMap<>();

    /**
     * Checks if id is spamming.
     *
     * @param id       The id to check.
     * @param time     The time in milliseconds.
     * @param timeUnit The time unit.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, int time, @Nonnull TimeUnit timeUnit) {
        return HSpam.spam(id, timeUnit.toMillis(time));
    }

    /**
     * Checks if id is spamming.
     *
     * @param id   The id to check.
     * @param time The time in milliseconds.
     * @return True if spamming.
     */
    public static boolean spam(@Nonnull String id, long time) {
        if (!spamMap.containsKey(id)) {
            spamMap.put(id, System.currentTimeMillis());
            HCore.syncScheduler().after(time / 50)
                    .run(() -> spamMap.remove(id));
            return false;
        }
        return true;
    }
}