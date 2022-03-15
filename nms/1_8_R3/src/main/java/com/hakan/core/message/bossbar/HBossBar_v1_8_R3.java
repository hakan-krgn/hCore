package com.hakan.core.message.bossbar;

import org.bukkit.entity.Player;

import java.util.*;

public class HBossBar_v1_8_R3 implements HBossBar {

    private String title;
    private HBarColor color;
    private HBarStyle style;
    private double progress;
    private final Set<HBarFlag> flags;

    public HBossBar_v1_8_R3(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        this.title = title;
        this.color = color;
        this.style = style;
        this.flags = new HashSet<>(Arrays.asList(flags));
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public HBarColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(HBarColor color) {
        this.color = color;
    }

    @Override
    public HBarStyle getStyle() {
        return this.style;
    }

    @Override
    public void setStyle(HBarStyle style) {
        this.style = style;
    }

    @Override
    public void removeFlag(HBarFlag flag) {
        this.flags.remove(flag);
    }

    @Override
    public void addFlag(HBarFlag flag) {
        this.flags.add(flag);
    }

    @Override
    public boolean hasFlag(HBarFlag flag) {
        return this.flags.contains(flag);
    }

    @Override
    public void setProgress(double progress) {
        this.progress = progress;
    }

    @Override
    public double getProgress() {
        return this.progress;
    }

    @Override
    public void addPlayer(Player player) {
    }

    @Override
    public void removePlayer(Player player) {
    }

    @Override
    public void removeAll() {
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.emptyList();
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
