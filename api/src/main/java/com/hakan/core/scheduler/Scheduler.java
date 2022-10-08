package com.hakan.core.scheduler;

import com.hakan.core.HCore;
import com.hakan.core.utils.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Scheduler class.
 */
public final class Scheduler {

    private static final List<Scheduler> schedulers = new LinkedList<>();

    public static void initialize() {
        HCore.asyncScheduler().every(1).run(() -> {
            for (Scheduler scheduler : new ArrayList<>(schedulers)) {
                if (scheduler.isCancelled()) {
                    if (scheduler.endRunnable != null)
                        scheduler.endRunnable.run();
                    schedulers.remove(scheduler);
                }
            }
        });
    }



    private final JavaPlugin plugin;
    private final List<Function<BukkitRunnable, Boolean>> freezeFilters;
    private final List<Function<BukkitRunnable, Boolean>> terminateFilters;

    private boolean async;
    private Runnable endRunnable;
    private BukkitRunnable runnable;

    private long after;
    private long every;

    private long end;
    private long start;
    private long counter;

    private long limiter;

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin.
     * @param async  Async.
     */
    public Scheduler(@Nonnull JavaPlugin plugin, boolean async) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
        this.async = async;

        this.after = 0;
        this.every = -1;

        this.end = -1;
        this.start = -1;
        this.counter = 0;

        this.limiter = -1;

        this.freezeFilters = new LinkedList<>();
        this.terminateFilters = new LinkedList<>();
    }

    /**
     * Gets task id of
     * bukkit runnable.
     *
     * @return Task id.
     */
    public int getTaskId() {
        return (this.runnable != null) ? this.runnable.getTaskId() : -1;
    }

    /**
     * Checks if scheduler is
     * cancelled or not.
     *
     * @return True if cancelled.
     */
    public boolean isCancelled() {
        return !Bukkit.getScheduler().isCurrentlyRunning(this.getTaskId()) &&
                !Bukkit.getScheduler().isQueued(this.getTaskId());
    }

    /**
     * Sets async mode of scheduler.
     *
     * @param async Async mode.
     * @return This class.
     */
    @Nonnull
    public Scheduler async(boolean async) {
        this.async = async;
        return this;
    }

    /**
     * Sets scheduler start and end
     * count. Then scheduler will be
     * started at start count, and
     * it will be terminated at end count.
     *
     * @param start Start count.
     * @param end   End count.
     * @return This class.
     */
    @Nonnull
    public Scheduler between(long start, long end) {
        this.counter = Math.max(0, start);
        this.start = Math.max(0, start);
        this.end = Math.max(0, end);
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
    @Nonnull
    public Scheduler limit(long limiter) {
        this.limiter = Math.max(limiter, 0);
        return this;
    }

    /**
     * Runs how many ticks later.
     *
     * @param after Ticks.
     * @return This class.
     */
    @Nonnull
    public Scheduler after(long after) {
        this.after = Math.max(after, 0);
        return this;
    }

    /**
     * Runs every how many ticks.
     *
     * @param every Ticks.
     * @return This class.
     */
    @Nonnull
    public Scheduler every(long every) {
        this.every = Math.max(every, -1);
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
    public Scheduler after(long after, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null!");
        return this.after(timeUnit.toMillis(after) / 50);
    }

    /**
     * Runs every how much time.
     *
     * @param every    Ticks.
     * @param timeUnit Time unit.
     * @return This class.
     */
    @Nonnull
    public Scheduler every(long every, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeUnit, "time unit cannot be null!");
        return this.every(timeUnit.toMillis(every) / 50);
    }

    /**
     * Runs how many ticks later.
     *
     * @param duration Duration.
     * @return This class.
     */
    @Nonnull
    public Scheduler after(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        return this.after(duration.toMillis() / 50);
    }

    /**
     * Runs every how many ticks.
     *
     * @param duration Duration.
     * @return This class.
     */
    @Nonnull
    public Scheduler every(@Nonnull Duration duration) {
        Validate.notNull(duration, "duration cannot be null!");
        return this.every(duration.toMillis() / 50);
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
    public Scheduler freezeIf(@Nonnull Function<BukkitRunnable, Boolean> freezeFilter) {
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
    public Scheduler terminateIf(@Nonnull Function<BukkitRunnable, Boolean> terminateFilter) {
        this.terminateFilters.add(Validate.notNull(terminateFilter, "terminate filter cannot be null!"));
        return this;
    }

    /**
     * Given runnable will be executed
     * when scheduler is terminated.
     *
     * @param runnable Runnable.
     * @return This class.
     */
    @Nonnull
    public Scheduler whenEnded(@Nonnull Runnable runnable) {
        this.endRunnable = Validate.notNull(runnable, "end runnable cannot be null!");
        return this;
    }

    /**
     * Cancels the runnable
     * if it's currently running.
     *
     * @return This class.
     */
    @Nonnull
    public synchronized Scheduler cancel() {
        if (this.runnable == null)
            return this;
        if (this.endRunnable != null)
            this.endRunnable.run();

        this.runnable.cancel();
        schedulers.remove(this);
        return this;
    }

    /**
     * Starts to scheduler.
     *
     * @param runnable Callback.
     * @return This class.
     */
    @Nonnull
    public synchronized Scheduler run(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "runnable cannot be null!");
        return this.run((task) -> runnable.run());
    }

    /**
     * Starts to scheduler.
     *
     * @param consumer Callback.
     * @return This class.
     */
    @Nonnull
    public synchronized Scheduler run(@Nonnull Consumer<BukkitRunnable> consumer) {
        Validate.notNull(consumer, "consumer cannot be null!");
        return this.run((task, count) -> consumer.accept(task));
    }

    /**
     * Starts to scheduler.
     *
     * @param taskConsumer Callback.
     * @return This class.
     */
    @Nonnull
    public synchronized Scheduler run(@Nonnull BiConsumer<BukkitRunnable, Long> taskConsumer) {
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
                        Scheduler.this.cancel();
                        return;
                    }
                }


                if (every == -1) {
                    taskConsumer.accept(this, counter);
                    return;
                } else if (limiter != -1 && limiter-- <= 0) {
                    Scheduler.this.cancel();
                    return;
                }


                if (start == -1 && end == -1) {
                    taskConsumer.accept(this, counter++);
                } else if (start == end) {
                    taskConsumer.accept(this, counter++);
                    Scheduler.this.cancel();
                } else if (start < end) {
                    if (counter >= start && counter <= end) taskConsumer.accept(this, counter++);
                    if (counter > end) Scheduler.this.cancel();
                } else {
                    if (counter <= start && counter >= end) taskConsumer.accept(this, counter--);
                    if (counter < end) Scheduler.this.cancel();
                }
            }
        };

        if (this.async) this.runnable.runTaskTimer(this.plugin, this.after, this.every);
        else this.runnable.runTaskTimerAsynchronously(this.plugin, this.after, this.every);


        schedulers.add(this);
        return this;
    }
}