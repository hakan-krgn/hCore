package com.hakan.core.message.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_13_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;

import java.util.List;

public class HBossBar_v1_13_R1 implements HBossBar {

    private final BossBar bossBar;

    public HBossBar_v1_13_R1(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        this.bossBar = new CraftBossBar(title, BarColor.valueOf(color.name()), BarStyle.valueOf(style.name()));
        for (HBarFlag hBarFlag : flags)
            this.bossBar.addFlag(BarFlag.valueOf(hBarFlag.name()));
    }

    @Override
    public String getTitle() {
        return this.bossBar.getTitle();
    }

    @Override
    public void setTitle(String s) {
        this.bossBar.setTitle(s);
    }

    @Override
    public HBarColor getColor() {
        return HBarColor.valueOf(this.bossBar.getColor().name());
    }

    @Override
    public void setColor(HBarColor barColor) {
        this.bossBar.setColor(BarColor.valueOf(barColor.name()));
    }

    @Override
    public HBarStyle getStyle() {
        return HBarStyle.valueOf(this.bossBar.getStyle().name());
    }

    @Override
    public void setStyle(HBarStyle barStyle) {
        this.bossBar.setStyle(BarStyle.valueOf(barStyle.name()));
    }

    @Override
    public void removeFlag(HBarFlag barFlag) {
        this.bossBar.removeFlag(BarFlag.valueOf(barFlag.name()));
    }

    @Override
    public void addFlag(HBarFlag barFlag) {
        this.bossBar.addFlag(BarFlag.valueOf(barFlag.name()));
    }

    @Override
    public boolean hasFlag(HBarFlag barFlag) {
        return this.bossBar.hasFlag(BarFlag.valueOf(barFlag.name()));
    }

    @Override
    public void setProgress(double progress) {
        this.bossBar.setProgress(progress);
    }

    @Override
    public double getProgress() {
        return this.bossBar.getProgress();
    }

    @Override
    public void addPlayer(Player player) {
        this.bossBar.addPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        this.bossBar.removePlayer(player);
    }

    @Override
    public void removeAll() {
        this.bossBar.removeAll();
    }

    @Override
    public List<Player> getPlayers() {
        return this.bossBar.getPlayers();
    }

    @Override
    public void setVisible(boolean b) {
        this.bossBar.setVisible(b);
    }

    @Override
    public boolean isVisible() {
        return this.bossBar.isVisible();
    }
}