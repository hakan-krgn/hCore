package com.hakan.core.message.bossbar.versions;

import com.hakan.core.message.bossbar.BossBar;
import com.hakan.core.message.bossbar.meta.BarColor;
import com.hakan.core.message.bossbar.meta.BarFlag;
import com.hakan.core.message.bossbar.meta.BarStyle;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public class BossBarEmpty implements BossBar {

    private String title;
    private BarColor color;
    private BarStyle style;
    private double progress;
    private final Set<BarFlag> flags;

    /**
     * {@inheritDoc}
     */
    public BossBarEmpty(@Nonnull String title,
                        @Nonnull BarColor color,
                        @Nonnull BarStyle style,
                        @Nonnull BarFlag... flags) {
        this.title = Validate.notNull(title, "title cannot be null!");
        this.color = Validate.notNull(color, "bar color cannot be null!");
        this.style = Validate.notNull(style, "bar style cannot be null!");
        this.flags = new HashSet<>(Arrays.asList(Validate.notNull(flags, "bar flags cannot be null!")));
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
        this.title = Validate.notNull(title, "title cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public BarColor getColor() {
        return this.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(@Nonnull BarColor color) {
        this.color = Validate.notNull(color, "border color cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public BarStyle getStyle() {
        return this.style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyle(@Nonnull BarStyle style) {
        this.style = Validate.notNull(style, "border style cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFlag(@Nonnull BarFlag flag) {
        this.flags.remove(Validate.notNull(flag, "border flag cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFlag(@Nonnull BarFlag flag) {
        this.flags.add(Validate.notNull(flag, "border flag cannot be null!"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFlag(@Nonnull BarFlag flag) {
        return this.flags.contains(Validate.notNull(flag, "border flag cannot be null!"));
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
        Validate.notNull(player, "player cannot be null!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(@Nonnull Player player) {
        Validate.notNull(player, "player cannot be null!");
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
