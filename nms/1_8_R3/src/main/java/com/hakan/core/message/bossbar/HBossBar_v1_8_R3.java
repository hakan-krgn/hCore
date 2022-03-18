package com.hakan.core.message.bossbar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * {@inheritDoc}
 */
public final class HBossBar_v1_8_R3 implements HBossBar {

    private String title;
    private HBarColor color;
    private HBarStyle style;
    private double progress;
    private final Set<HBarFlag> flags;

    /**
     * {@inheritDoc}
     */
    public HBossBar_v1_8_R3(@Nonnull String title, @Nonnull HBarColor color, @Nonnull HBarStyle style, @Nonnull HBarFlag... flags) {
        this.title = Objects.requireNonNull(title, "title cannot be null!");
        this.color = Objects.requireNonNull(color, "bar color cannot be null!");
        this.style = Objects.requireNonNull(style, "bar style cannot be null!");
        this.flags = new HashSet<>(Arrays.asList(Objects.requireNonNull(flags, "bar flags cannot be null!")));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(@Nonnull String title) {
        this.title = Objects.requireNonNull(title, "title cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HBarColor getColor() {
        return this.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(@Nonnull HBarColor color) {
        this.color = Objects.requireNonNull(color, "border color cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public HBarStyle getStyle() {
        return this.style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyle(@Nonnull HBarStyle style) {
        this.style = Objects.requireNonNull(style, "border style cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFlag(@Nonnull HBarFlag flag) {
        this.flags.remove(Objects.requireNonNull(flag, "border flag cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFlag(@Nonnull HBarFlag flag) {
        this.flags.add(Objects.requireNonNull(flag, "border flag cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFlag(@Nonnull HBarFlag flag) {
        return this.flags.contains(Objects.requireNonNull(flag, "border flag cannot be null!"));
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
    public void addPlayer(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(@Nonnull Player player) {
        Objects.requireNonNull(player, "player cannot be null!");
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
    @Nonnull
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