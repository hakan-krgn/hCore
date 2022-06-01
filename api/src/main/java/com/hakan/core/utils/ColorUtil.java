package com.hakan.core.utils;

import com.hakan.core.HCore;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ColorUtil class to convert color
 * codes in messages.
 */
public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F\\d]{6}");

    /**
     * Convert a message to a colored message.
     *
     * @param message The message to convert.
     * @return The colored message.
     */
    @Nonnull
    public static String colored(@Nonnull String message) {
        Objects.requireNonNull(message, "message cannot be null!");

        if (HCore.getProtocolVersion().isNewerOrEqual(ProtocolVersion.v1_16_R1)) {
            Matcher matcher = HEX_PATTERN.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                matcher = HEX_PATTERN.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Convert hex color code to chat color.
     *
     * @param hex The hex color code.
     * @return The chat color.
     */
    @Nonnull
    public static String color(@Nonnull String hex) {
        Objects.requireNonNull(hex, "hex color code cannot be null!");
        return colored(hex);
    }

    /**
     * Convert rgb to chat color.
     *
     * @param r The red value.
     * @param b The blue value.
     * @param g The green value.
     * @return The chat color.
     */
    @Nonnull
    public static String color(float r, float b, float g) {
        return color(new Color(r, b, g));
    }

    /**
     * Convert color to chat color.
     *
     * @param r The red value.
     * @param b The blue value.
     * @param g The green value.
     * @param a The alpha value.
     * @return The chat color.
     */
    @Nonnull
    public static String color(float r, float b, float g, float a) {
        return color(new Color(r, b, g, a));
    }

    /**
     * Convert color to chat color.
     *
     * @param color The color.
     * @return The chat color.
     */
    @Nonnull
    public static String color(@Nonnull Color color) {
        Objects.requireNonNull(color, "color cannot be null!");
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return color(hex);
    }
}