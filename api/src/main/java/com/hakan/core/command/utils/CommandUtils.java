package com.hakan.core.command.utils;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * CommandUtils class to check some
 * conditions.
 */
public final class CommandUtils {

    /**
     * Checks if the args2 contains args1.
     *
     * @param args1 Argument list 1.
     * @param args2 Argument list 2.
     * @return True if args2 contains args1.
     */
    public static boolean isMatching(@Nonnull String[] args1, @Nonnull String[] args2) {
        Objects.requireNonNull(args1, "args1 cannot be null!");
        Objects.requireNonNull(args2, "args2 cannot be null!");

        if (args2.length < args1.length)
            return false;
        for (int i = 0; i < args1.length; i++)
            if (!args1[i].equalsIgnoreCase(args2[i]))
                return false;
        return true;
    }
}