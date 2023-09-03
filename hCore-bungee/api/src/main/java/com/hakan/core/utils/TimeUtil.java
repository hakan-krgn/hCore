package com.hakan.core.utils;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtil class to format dates.
 */
public final class TimeUtil {

    /**
     * Formats the now to the given pattern.
     *
     * @param pattern The pattern.
     * @return The formatted date.
     */
    @Nonnull
    public static String formatNow(@Nonnull String pattern) {
        return TimeUtil.formatDate(new Date(), pattern);
    }

    /**
     * Formats the given date to the given pattern.
     *
     * @param time    The time to format.
     * @param pattern The pattern to format the time to.
     * @return The formatted time.
     */
    @Nonnull
    public static String formatDate(long time, @Nonnull String pattern) {
        return TimeUtil.formatDate(new Date(time), pattern);
    }

    /**
     * Formats the given date to the given pattern.
     *
     * @param date    The date to format.
     * @param pattern The pattern to format the date to.
     * @return The formatted date.
     */
    @Nonnull
    public static String formatDate(@Nonnull Date date, @Nonnull String pattern) {
        Validate.notNull(date, "date cannot be null!");
        Validate.notNull(pattern, "pattern cannot be null!");

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * Formats the time to the given pattern.
     *
     * @param time    The time in milliseconds.
     * @param pattern The pattern to use.
     * @return The formatted time.
     */
    @Nonnull
    public static String formatTime(long time, @Nonnull String pattern) {
        Validate.notNull(pattern, "pattern cannot be null!");

        long days = time / 86400000;
        long hours = (time % 86400000) / 3600000;
        long minutes = (time % 3600000) / 60000;
        long seconds = (time % 60000) / 1000;

        return pattern.replaceAll("d+", days < 10 ? "0" + days : "" + days)
                .replaceAll("H+", hours < 10 ? "0" + hours : "" + hours)
                .replaceAll("m+", minutes < 10 ? "0" + minutes : "" + minutes)
                .replaceAll("s+", seconds < 10 ? "0" + seconds : "" + seconds);
    }
}
