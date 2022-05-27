package com.hakan.core.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * HScheduler class.
 */
public final class HScheduler {

    private final JavaPlugin plugin;
    private final List<Function<BukkitRunnable, Boolean>> freezeFilters;
    private final List<Function<BukkitRunnable, Boolean>> terminateFilters;
    private long after;
    private Long every;
    private Long limiter;
    private boolean async;

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin.
     * @param async  Async.
     */
    public HScheduler(@Nonnull JavaPlugin plugin, boolean async) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null!");
        this.async = async;
        this.freezeFilters = new ArrayList<>();
        this.terminateFilters = new ArrayList<>();
    }

    /**
     * Checks will scheduler run as async?
     *
     * @return If scheduler run as async, returns true.
     */
    public boolean isAsync() {
        return this.async;
    }

    /**
     * Sets async mode of scheduler.
     *
     * @param async Async mode.
     * @return This class.
     */
    @Nonnull
    public HScheduler async(boolean async) {
        this.async = async;
        return this;
    }

    /**
     * Sets scheduler limit.
     * If limit is set, scheduler will be
     * terminated after limit is reached.
     *
     * @param limiter Limit.
     * @return This class.
     */
    public HScheduler limit(long limiter) {
        this.limiter = limiter;
        return this;
    }

    /**
     * Runs how many ticks later.
     *
     * @param after Ticks.
     * @return This class.
     */
    @Nonnull
    public HScheduler after(long after) {
        this.after = after;
        return this;
    }

    /**
     * Runs every how many ticks.
     *
     * @param every Ticks.
     * @return This class.
     */
    @Nonnull
    public HScheduler every(long every) {
        this.every = every;
        return this;
    }

    /**
     * Runs how many ticks later.
     *
     * @param after    Ticks.
     * @param timeUnit Time unit.
     * @return This class.
     */
    @Nonnull
    public HScheduler after(int after, @Nonnull TimeUnit timeUnit) {
        this.after = timeUnit.toSeconds(after) * 20;
        return this;
    }

    /**
     * Runs every how many ticks.
     *
     * @param every    Ticks.
     * @param timeUnit Time unit.
     * @return This class.
     */
    @Nonnull
    public HScheduler every(int every, @Nonnull TimeUnit timeUnit) {
        this.every = timeUnit.toSeconds(every) * 20;
        return this;
    }

    /**
     * Adds run filter.
     * If filter returns true,
     * scheduler will freeze itself.
     *
     * @param freezeFilter Freeze filter.
     * @return This class.
     */
    @Nonnull
    public HScheduler freezeIf(@Nonnull Function<BukkitRunnable, Boolean> freezeFilter) {
        this.freezeFilters.add(Objects.requireNonNull(freezeFilter, "freeze filter cannot be null!"));
        return this;
    }

    /**
     * Adds terminate filter.
     * If this filter returns true,
     * scheduler will be terminated.
     *
     * @param terminateFilter Terminate filter.
     * @return This class.
     */
    @Nonnull
    public HScheduler terminateIf(@Nonnull Function<BukkitRunnable, Boolean> terminateFilter) {
        this.terminateFilters.add(Objects.requireNonNull(terminateFilter, "terminate filter cannot be null!"));
        return this;
    }

    /**
     * Starts to scheduler.
     *
     * @param runnable Callback.
     * @return Bukkit task id.
     */
    public synchronized int run(@Nonnull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable cannot be null!");
        return this.run(consumer -> runnable.run());
    }

    /**
     * Starts to scheduler.
     *
     * @param taskConsumer Callback.
     * @return Bukkit task id.
     */
    public synchronized int run(@Nonnull Consumer<BukkitRunnable> taskConsumer) {
        Objects.requireNonNull(taskConsumer, "task consumer cannot be null!");

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (this.isCancelled())
                    return;

                for (Function<BukkitRunnable, Boolean> freezeFilter : freezeFilters) {
                    if (freezeFilter.apply(this)) {
                        return;
                    }
                }

                for (Function<BukkitRunnable, Boolean> terminateFilter : terminateFilters) {
                    if (terminateFilter.apply(this)) {
                        this.cancel();
                        return;
                    }
                }

                taskConsumer.accept(this);

                if (limiter != null) {
                    limiter--;
                    if (limiter <= 0)
                        this.cancel();
                }
            }
        };

        if (this.async) {
            if (this.every == null)
                return bukkitRunnable.runTaskLaterAsynchronously(this.plugin, this.after).getTaskId();
            else
                return bukkitRunnable.runTaskTimerAsynchronously(this.plugin, this.after, this.every).getTaskId();
        } else {
            if (this.every == null)
                return bukkitRunnable.runTaskLater(this.plugin, this.after).getTaskId();
            else
                return bukkitRunnable.runTaskTimer(this.plugin, this.after, this.every).getTaskId();
        }
    }
}