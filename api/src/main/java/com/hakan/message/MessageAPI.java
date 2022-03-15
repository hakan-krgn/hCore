package com.hakan.message;

import com.hakan.HCore;
import com.hakan.message.actionbar.ActionBarHandler;
import com.hakan.message.bossbar.HBarColor;
import com.hakan.message.bossbar.HBarFlag;
import com.hakan.message.bossbar.HBarStyle;
import com.hakan.message.bossbar.HBossBar;
import com.hakan.message.title.HTitle;
import com.hakan.message.title.TitleHandler;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MessageAPI {

    private static ActionBarHandler actionBarHandler;
    private static TitleHandler titleHandler;

    public static void initialize() {
        try {
            String version = HCore.getVersionString();
            MessageAPI.actionBarHandler = (ActionBarHandler) Class.forName("com.hakan.message.actionbar.ActionBarHandler_" + version).getConstructor().newInstance();
            MessageAPI.titleHandler = (TitleHandler) Class.forName("com.hakan.message.title.TitleHandler_" + version).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    TITLE
     */
    public static void sendTitle(Player player, HTitle hTitle) {
        MessageAPI.titleHandler.send(player, hTitle);
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        MessageAPI.sendTitle(player, new HTitle(title, subTitle));
    }

    public static void sendTitle(Player player, String title, String subTitle, int stay, int fadein, int fadeout) {
        MessageAPI.sendTitle(player, new HTitle(title, subTitle, stay, fadein, fadeout));
    }

    public static void sendTitle(Collection<Player> players, HTitle hTitle) {
        players.forEach(player -> MessageAPI.titleHandler.send(player, hTitle));
    }

    public static void sendTitle(Collection<Player> players, String title, String subTitle) {
        MessageAPI.sendTitle(players, new HTitle(title, subTitle));
    }

    public static void sendTitle(Collection<Player> players, String title, String subTitle, int stay, int fadein, int fadeout) {
        MessageAPI.sendTitle(players, new HTitle(title, subTitle, stay, fadein, fadeout));
    }

    /*
    ACTION BAR
     */
    public static void sendActionBar(Player player, String text) {
        MessageAPI.actionBarHandler.send(player, text);
    }

    public static void sendActionBar(Collection<Player> players, String text) {
        players.forEach(player -> MessageAPI.sendActionBar(player, text));
    }


    /*
    BOSS BAR
     */
    public static HBossBar createBossBar(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        try {
            String version = HCore.getVersionString();
            return (HBossBar) Class.forName("com.hakan.message.bossbar.HBossBar_" + version)
                    .getConstructor(String.class, HBarColor.class, HBarStyle.class, HBarFlag[].class).newInstance(title, color, style, flags);
        } catch (Exception e) {
            return null;
        }
    }

    public static HBossBar createBossBar(String title, HBarColor color, HBarStyle style) {
        return MessageAPI.createBossBar(title, color, style, new HBarFlag[0]);
    }
}