package com.hakan.message.bossbar;

import org.bukkit.entity.Player;

import java.util.List;

public interface HBossBar {

    String getTitle();

    void setTitle(String var1);

    HBarColor getColor();

    void setColor(HBarColor var1);

    HBarStyle getStyle();

    void setStyle(HBarStyle var1);

    void removeFlag(HBarFlag var1);

    void addFlag(HBarFlag var1);

    boolean hasFlag(HBarFlag var1);

    void setProgress(double var1);

    double getProgress();

    void addPlayer(Player var1);

    void removePlayer(Player var1);

    void removeAll();

    List<Player> getPlayers();

    void setVisible(boolean var1);

    boolean isVisible();

}