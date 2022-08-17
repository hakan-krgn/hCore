package com.hakan.core.scheduler;

import com.hakan.core.utils.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
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
    private BukkitRunnable runnable;
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
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
        this.async = async;
        this.freezeFilters = new LinkedList<>();
        this.terminateFilters = new LinkedList<>();
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
     * Runs how much time later.
     *
     * @param after    Ticks.
     * @param timeUnit Time unit.
     * @return This class.
     */
    @Nonnull
    public HScheduler after(int after, @Nonnull TimeUnit timeUnit) {
        this.after = timeUnit.toMillis(after) / 50;
        return this;
    }

    /**
     * Runs every how much time.
     *
     * @param every    Ticks.
     * @param timeUnit Time unit.
     * @return This class.
     */
    @Nonnull
    public HScheduler every(int every, @Nonnull TimeUnit timeUnit) {
        this.every = timeUnit.toMillis(every) / 50;
        return this;
    }

    /**
     * Runs how many ticks later.
     *
     * @param duration Duration.
     * @return This class.
     */
    @Nonnull
    public HScheduler after(@Nonnull Duration duration) {
        this.after = duration.toMillis() / 50;
        return this;
    }

    /**
     * Runs every how many ticks.
     *
     * @param duration Duration.
     * @return This class.
     */
    @Nonnull
    public HScheduler every(@Nonnull Duration duration) {
        this.every = duration.toMillis() / 50;
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
        this.freezeFilters.add(Validate.notNull(freezeFilter, "freeze filter cannot be null!"));
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
        this.terminateFilters.add(Validate.notNull(terminateFilter, "terminate filter cannot be null!"));
        return this;
    }

    /**
     * Cancels the runnable if it's running.
     */
    public synchronized void cancel() {
        if (this.runnable != null)
            this.runnable.cancel();
    }

    /**
     * Starts to scheduler.
     *
     * @param runnable Callback.
     * @return Bukkit task id.
     */
    public synchronized int run(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "runnable cannot be null!");
        return this.run(consumer -> runnable.run());
    }

    /**
     * Starts to scheduler.
     *
     * @param taskConsumer Callback.
     * @return Bukkit task id.
     */
    public synchronized int run(@Nonnull Consumer<BukkitRunnable> taskConsumer) {
        Validate.notNull(taskConsumer, "task consumer cannot be null!");

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
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
                return this.runnable.runTaskLaterAsynchronously(this.plugin, this.after).getTaskId();
            else
                return this.runnable.runTaskTimerAsynchronously(this.plugin, this.after, this.every).getTaskId();
        } else {
            if (this.every == null)
                return this.runnable.runTaskLater(this.plugin, this.after).getTaskId();
            else
                return this.runnable.runTaskTimer(this.plugin, this.after, this.every).getTaskId();
        }
    }
}