package com.hakan.core.command.utils;

import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CommandUtils class to check some
 * conditions.
 */
public final class CommandUtils {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(?<=<%)(?<placeholder>.+?)(?=%>)");

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
            if (!(args1[i].equalsIgnoreCase(args2[i]) || CommandUtils.hasPlaceholder(args1[i])))
                return false;
        return true;
    }

    /**
     * Gets the placeholder from
     * the given string.
     *
     * @param arg Argument.
     * @return Placeholder.
     */
    @Nullable
    public static String getPlaceholder(@Nonnull String arg) {
        Validate.notNull(arg, "arg cannot be null!");
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(arg);
        return matcher.find() ? matcher.group("placeholder") : null;
    }

    /**
     * Checks if the arg
     * contains a placeholder.
     *
     * @param arg Argument.
     * @return True if arg contains a placeholder, returns true.
     */
    public static boolean hasPlaceholder(@Nonnull String arg) {
        Validate.notNull(arg, "arg cannot be null!");
        return PLACEHOLDER_PATTERN.matcher(arg).find();
    }

    /**
     * Checks if the sender has
     * the given permission.
     *
     * @param sender     Sender.
     * @param permission Permission.
     * @return True if sender has the given permission.
     */
    public static boolean hasPermission(@Nonnull CommandSender sender, @Nonnull String permission) {
        Validate.notNull(permission, "permission cannot be null!");
        return permission.equals("") ||
               sender.hasPermission("*") ||
               sender.hasPermission(permission);
    }
}
