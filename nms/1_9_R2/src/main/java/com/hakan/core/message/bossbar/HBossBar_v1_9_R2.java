package com.hakan.core.message.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_9_R2.boss.CraftBossBar;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class HBossBar_v1_9_R2 implements HBossBar {

    private final BossBar bossBar;

    /**
     * {@inheritDoc}
     */
    public HBossBar_v1_9_R2(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        this.bossBar = new CraftBossBar(title, BarColor.valueOf(color.name()), BarStyle.valueOf(style.name()));
        for (HBarFlag hBarFlag : flags)
            this.bossBar.addFlag(BarFlag.valueOf(hBarFlag.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getTitle() {
        return this.bossBar.getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(@Nonnull String s) {
        this.bossBar.setTitle(s);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HBarColor getColor() {
        return HBarColor.valueOf(this.bossBar.getColor().name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(@Nonnull HBarColor barColor) {
        this.bossBar.setColor(BarColor.valueOf(barColor.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HBarStyle getStyle() {
        return HBarStyle.valueOf(this.bossBar.getStyle().name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyle(@Nonnull HBarStyle barStyle) {
        this.bossBar.setStyle(BarStyle.valueOf(barStyle.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFlag(@Nonnull HBarFlag barFlag) {
        this.bossBar.removeFlag(BarFlag.valueOf(barFlag.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFlag(@Nonnull HBarFlag barFlag) {
        this.bossBar.addFlag(BarFlag.valueOf(barFlag.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFlag(@Nonnull HBarFlag barFlag) {
        return this.bossBar.hasFlag(BarFlag.valueOf(barFlag.name()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgress(double progress) {
        this.bossBar.setProgress(progress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getProgress() {
        return this.bossBar.getProgress();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPlayer(@Nonnull Player player) {
        this.bossBar.addPlayer(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(@Nonnull Player player) {
        this.bossBar.removePlayer(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        this.bossBar.removeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public List<Player> getPlayers() {
        return this.bossBar.getPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean b) {
        this.bossBar.setVisible(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return this.bossBar.isVisible();
    }
}