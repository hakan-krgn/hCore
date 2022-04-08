package com.hakan.core.spam;

import com.hakan.core.HCore;

import javax.annotation.Nonnull;
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
        if (!HSpam.spams.contains(id)) {
            HSpam.spams.add(id);
            HCore.syncScheduler().after(time / 50)
                    .run(() -> HSpam.spams.remove(id));
            return false;
        }
        return true;
    }
}