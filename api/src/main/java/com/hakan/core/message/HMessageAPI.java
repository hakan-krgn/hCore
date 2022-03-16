package com.hakan.core.message;

import com.hakan.core.HCore;
import com.hakan.core.message.actionbar.HActionBarHandler;
import com.hakan.core.message.bossbar.HBarColor;
import com.hakan.core.message.bossbar.HBarFlag;
import com.hakan.core.message.bossbar.HBarStyle;
import com.hakan.core.message.bossbar.HBossBar;
import com.hakan.core.message.title.HTitle;
import com.hakan.core.message.title.HTitleHandler;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HMessageAPI {

    private static HActionBarHandler hActionBarHandler;
    private static HTitleHandler hTitleHandler;

    public static void initialize() {
        try {
            String version = HCore.getVersionString();
            HMessageAPI.hActionBarHandler = (HActionBarHandler) Class.forName("com.hakan.core.message.actionbar.HActionBarHandler_" + version).getConstructor().newInstance();
            HMessageAPI.hTitleHandler = (HTitleHandler) Class.forName("com.hakan.core.message.title.HTitleHandler_" + version).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    TITLE
     */
    public static void sendTitle(Player player, HTitle hTitle) {
        HMessageAPI.hTitleHandler.send(player, hTitle);
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        HMessageAPI.sendTitle(player, new HTitle(title, subTitle));
    }

    public static void sendTitle(Player player, String title, String subTitle, int stay, int fadein, int fadeout) {
        HMessageAPI.sendTitle(player, new HTitle(title, subTitle, stay, fadein, fadeout));
    }

    public static void sendTitle(Collection<Player> players, HTitle hTitle) {
        players.forEach(player -> HMessageAPI.hTitleHandler.send(player, hTitle));
    }

    public static void sendTitle(Collection<Player> players, String title, String subTitle) {
        HMessageAPI.sendTitle(players, new HTitle(title, subTitle));
    }

    public static void sendTitle(Collection<Player> players, String title, String subTitle, int stay, int fadein, int fadeout) {
        HMessageAPI.sendTitle(players, new HTitle(title, subTitle, stay, fadein, fadeout));
    }


    /*
    ACTION BAR
     */
    public static void sendActionBar(Player player, String text) {
        HMessageAPI.hActionBarHandler.send(player, text);
    }

    public static void sendActionBar(Collection<Player> players, String text) {
        players.forEach(player -> HMessageAPI.sendActionBar(player, text));
    }


    /*
    BOSS BAR
     */
    public static HBossBar createBossBar(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        try {
            String version = HCore.getVersionString();
            return (HBossBar) Class.forName("com.hakan.core.message.bossbar.HBossBar_" + version)
                    .getConstructor(String.class, HBarColor.class, HBarStyle.class, HBarFlag[].class).newInstance(title, color, style, flags);
        } catch (Exception e) {
            return null;
        }
    }

    public static HBossBar createBossBar(String title, HBarColor color, HBarStyle style) {
        return HMessageAPI.createBossBar(title, color, style, new HBarFlag[0]);
    }
}