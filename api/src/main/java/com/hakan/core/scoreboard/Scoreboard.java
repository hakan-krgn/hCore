package com.hakan.core.scoreboard;

import com.hakan.core.HCore;
import com.hakan.core.scoreboard.wrapper.ScoreboardWrapper;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Scoreboard class for creating a
 * scoreboard object for player
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class Scoreboard {

    private final Player player;
    private final ScoreboardWrapper wrapper;
    private String title;
    private String[] lines;

    /**
     * Creates new Instance of this class.
     *
     * @param player Player.
     */
    public Scoreboard(@Nonnull Player player, @Nonnull String title) {
        this.wrapper = ReflectionUtils.newInstance("com.hakan.core.scoreboard.wrapper.ScoreboardWrapper_%s", this);
        this.player = Validate.notNull(player, "uid cannot be null!");
        this.title = Validate.notNull(title, "title cannot be null!");
        this.lines = new String[15];
    }

    /**
     * Checks if scoreboard still exist for player
     *
     * @return if scoreboard still exist for player, return true
     */
    public boolean isExist() {
        return ScoreboardHandler.has(this.player.getUniqueId());
    }

    /**
     * Gets UUID of player.
     *
     * @return UUID of player.
     */
    @Nonnull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets title of scoreboard.
     *
     * @return Title of scoreboard.
     */
    @Nonnull
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets title of scoreboard.
     *
     * @param title Title.
     */
    public Scoreboard setTitle(@Nonnull String title) {
        this.title = Validate.notNull(title, "title cannot be null!");
        return this;
    }

    /**
     * Gets lines of scoreboard.
     *
     * @return Lines of scoreboard.
     */
    @Nonnull
    public String[] getLines() {
        return this.lines;
    }

    /**
     * Gets text of line.
     *
     * @param line Line.
     * @return Text of line.
     */
    @Nullable
    public String getLine(int line) {
        return this.lines[line];
    }

    /**
     * Sets line of scoreboard to text.
     *
     * @param line Line number.
     * @param text Text.
     */
    public Scoreboard setLine(int line, @Nonnull String text) {
        this.lines[line] = Validate.notNull(text, "text cannot be null!");
        return this;
    }

    /**
     * Sets lines of scoreboard to lines.
     *
     * @param lines List of lines.
     */
    public Scoreboard setLines(@Nonnull List<String> lines) {
        return this.setLines(lines.toArray(new String[0]));
    }

    /**
     * Sets lines of scoreboard to lines.
     *
     * @param lines List of lines.
     */
    public Scoreboard setLines(@Nonnull String... lines) {
        Validate.isTrue(lines.length != 4, "lines length must be 4!");
        this.lines = Validate.notNull(lines, "lines cannot be null!");
        return this;
    }

    /**
     * Removes line from scoreboard.
     *
     * @param line Line number.
     */
    public Scoreboard removeLine(int line) {
        this.lines[line] = null;
        return this;
    }

    /**
     * When the time is up, scoreboard will
     * remove automatically.
     *
     * @param time     Time.
     * @param timeUnit Time unit (TimeUnit.SECONDS, TimeUnit.HOURS, etc.)
     * @return Instance of this class.
     */
    @Nonnull
    public Scoreboard expire(int time, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null");
        HCore.syncScheduler().after(time, timeUnit).run(this::delete);
        return this;
    }

    /**
     * When the time is up, scoreboard will
     * remove automatically.
     *
     * @param duration Duration.
     * @return Instance of this class.
     */
    @Nonnull
    public Scoreboard expire(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        HCore.syncScheduler().after(duration).run(this::delete);
        return this;
    }

    /**
     * When the time is up, scoreboard will
     * remove automatically.
     *
     * @param ticks Ticks.
     * @return Instance of this class.
     */
    @Nonnull
    public Scoreboard expire(int ticks) {
        HCore.syncScheduler().after(ticks)
                .run(this::delete);
        return this;
    }

    /**
     * Once every updateInterval ticks,
     * it will trigger.
     *
     * @param updateInterval Update interval as tick.
     * @return Instance of this class.
     */
    @Nonnull
    public Scoreboard update(int updateInterval) {
        return this.update(updateInterval, (board) -> {
        });
    }

    /**
     * Once every updateInterval ticks,
     * it will trigger.
     *
     * @param updateInterval Update interval as tick.
     * @param consumer       Callback.
     * @return Instance of this class.
     */
    @Nonnull
    public Scoreboard update(int updateInterval, @Nonnull Consumer<Scoreboard> consumer) {
        Validate.notNull(consumer, "consumer cannot be null!");
        Validate.isTrue(updateInterval <= 0, "update interval must be greater than 0!");

        HCore.syncScheduler().every(updateInterval)
                .terminateIf(task -> !this.isExist())
                .terminateIf(task -> !ScoreboardHandler.getByPlayer(this.player).equals(this))
                .run(() -> {
                    consumer.accept(this);
                    this.show();
                });
        return this;
    }

    /**
     * Shows the scoreboard to player.
     *
     * @return Instance of wrapper.
     */
    @Nonnull
    public Scoreboard show() {
        this.wrapper.show();
        return this;
    }

    /**
     * Deletes scoreboard.
     *
     * @return Instance of wrapper.
     */
    @Nonnull
    public Scoreboard delete() {
        this.wrapper.delete();
        return this;
    }
}