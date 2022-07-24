package com.hakan.core.command.utils;

import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

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
        Validate.notNull(args1, "args1 cannot be null!");
        Validate.notNull(args2, "args2 cannot be null!");

        if (args2.length < args1.length)
            return false;
        for (int i = 0; i < args1.length; i++)
            if (!args1[i].equalsIgnoreCase(args2[i]) && !args1[i].equals("<player>"))
                return false;
        return true;
    }
}