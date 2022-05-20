package com.hakan.core.utils;

import com.hakan.core.HCore;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ColorUtil class to convert color
 * codes in messages.
 */
public class ColorUtil {

    private static final Pattern PATTERN = Pattern.compile("#[a-fA-F\\d]{6}");

    /**
     * Convert a message to a colored message.
     *
     * @param message The message to convert.
     * @return The colored message.
     */
    public static String colored(String message) {
        if (HCore.getProtocolVersion().isOlder(ProtocolVersion.v1_16_R1)) {
            Matcher matcher = PATTERN.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                matcher = PATTERN.matcher(message);
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
    public static String color(String hex) {
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
    public static String color(float r, float b, float g, float a) {
        return color(new Color(r, b, g, a));
    }

    /**
     * Convert color to chat color.
     *
     * @param color The color.
     * @return The chat color.
     */
    public static String color(Color color) {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return color(hex);
    }
}