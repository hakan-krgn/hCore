package com.hakan.core.message.bossbar.versions;

import com.hakan.core.message.bossbar.BossBar;
import com.hakan.core.message.bossbar.meta.BarColor;
import com.hakan.core.message.bossbar.meta.BarFlag;
import com.hakan.core.message.bossbar.meta.BarStyle;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class BossBarImpl implements BossBar {

    private final org.bukkit.boss.BossBar bossBar;

    /**
     * {@inheritDoc}
     */
    public BossBarImpl(@Nonnull String title,
                       @Nonnull BarColor color,
                       @Nonnull BarStyle style,
                       @Nonnull BarFlag... flags) {
        Validate.notNull(title, "title cannot be null!");
        Validate.notNull(color, "color cannot be null!");
        Validate.notNull(style, "style cannot be null!");
        Validate.notNull(flags, "flags cannot be null!");

        this.bossBar = Bukkit.createBossBar(title, color.toBukkit(), style.toBukkit());
        Arrays.asList(flags).forEach((flag) -> this.bossBar.addFlag(flag.toBukkit()));
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
        this.bossBar.setTitle(Validate.notNull(s, "title cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public BarColor getColor() {
        return BarColor.valueOf(this.bossBar.getColor().name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(@Nonnull BarColor barColor) {
        this.bossBar.setColor(Validate.notNull(barColor, "bar color cannot be null!").toBukkit());
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public BarStyle getStyle() {
        return BarStyle.valueOf(this.bossBar.getStyle().name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyle(@Nonnull BarStyle barStyle) {
        this.bossBar.setStyle(Validate.notNull(barStyle, "bar style cannot be null!").toBukkit());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFlag(@Nonnull BarFlag barFlag) {
        this.bossBar.removeFlag(Validate.notNull(barFlag, "bar flag cannot be null!").toBukkit());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFlag(@Nonnull BarFlag barFlag) {
        this.bossBar.addFlag(Validate.notNull(barFlag, "bar flag cannot be null!").toBukkit());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFlag(@Nonnull BarFlag barFlag) {
        return this.bossBar.hasFlag(Validate.notNull(barFlag, "bar flag cannot be null!").toBukkit());
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
        this.bossBar.addPlayer(Validate.notNull(player, "player cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(@Nonnull Player player) {
        this.bossBar.removePlayer(Validate.notNull(player, "player cannot be null!"));
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
