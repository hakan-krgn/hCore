package com.hakan.core.message.bossbar;

import org.bukkit.entity.Player;

import java.util.*;

/**
 * {@inheritDoc}
 */
public class HBossBar_v1_8_R3 implements HBossBar {

    private String title;
    private HBarColor color;
    private HBarStyle style;
    private double progress;
    private final Set<HBarFlag> flags;

    /**
     * {@inheritDoc}
     */
    public HBossBar_v1_8_R3(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        this.title = title;
        this.color = color;
        this.style = style;
        this.flags = new HashSet<>(Arrays.asList(flags));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HBarColor getColor() {
        return this.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(HBarColor color) {
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HBarStyle getStyle() {
        return this.style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyle(HBarStyle style) {
        this.style = style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFlag(HBarFlag flag) {
        this.flags.remove(flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFlag(HBarFlag flag) {
        this.flags.add(flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFlag(HBarFlag flag) {
        return this.flags.contains(flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgress(double progress) {
        this.progress = progress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getProgress() {
        return this.progress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPlayer(Player player) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(Player player) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> getPlayers() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean visible) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return false;
    }
}
