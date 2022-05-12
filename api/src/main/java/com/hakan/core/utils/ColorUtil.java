package com.hakan.core.utils;

import com.hakan.core.HCore;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static final int SERVER_VERSION = Integer.parseInt(HCore.getVersionString()
            .replace("_", "")
            .replace("v", "")
            .replace("R", ""));

    public static String colored(String message) {
        if (SERVER_VERSION >= 161) {
            Pattern pattern = Pattern.compile("#[a-fA-F\\d]{6}");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                matcher = pattern.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}