package com.hakan.core.scheduler;

import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import javax.annotation.Nonnull;
import java.time.Duration;
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

    private final Plugin plugin;
    private final Thread thread;
    private final List<Function<ScheduledTask, Boolean>> freezeFilters;
    private final List<Function<ScheduledTask, Boolean>> terminateFilters;

    private boolean async;
    private Runnable endRunnable;
    private ScheduledTask runnable;

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
    public Scheduler(@Nonnull Plugin plugin, boolean async) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
        this.async = async;

        this.after = 0;
        this.every = -1;

        this.end = -1;
        this.start = -1;
        this.counter = 0;

        this.limiter = -1;

        this.thread = Thread.currentThread();
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
        return (this.runnable != null) ? this.runnable.getId() : -1;
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
        this.after = Math.max(after * 50, 0);
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
        this.every = Math.max(every * 50, -1);
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
    public Scheduler freezeIf(@Nonnull Function<ScheduledTask, Boolean> freezeFilter) {
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
    public Scheduler terminateIf(@Nonnull Function<ScheduledTask, Boolean> terminateFilter) {
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
    public synchronized Scheduler run(@Nonnull Consumer<ScheduledTask> consumer) {
        Validate.notNull(consumer, "consumer cannot be null!");
        return this.run((task, count) -> consumer.accept(task));
    }

    /**
     * Starts to scheduler.
     *
     * @param cons Callback.
     * @return This class.
     */
    @Nonnull
    public synchronized Scheduler run(@Nonnull BiConsumer<ScheduledTask, Long> cons) {
        Validate.notNull(cons, "task consumer cannot be null!");

        if (this.async && this.thread.equals(Thread.currentThread())) {
            this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> this.run(cons));
            return this;
        }


        this.runnable = this.plugin.getProxy().getScheduler().schedule(this.plugin, () -> {
            for (Function<ScheduledTask, Boolean> freezeFilter : this.freezeFilters) {
                if (freezeFilter.apply(this.runnable)) {
                    return;
                }
            }
            for (Function<ScheduledTask, Boolean> terminateFilter : this.terminateFilters) {
                if (terminateFilter.apply(this.runnable)) {
                    Scheduler.this.cancel();
                    return;
                }
            }


            if (this.every == -1) {
                cons.accept(this.runnable, this.counter);
                return;
            } else if (this.limiter != -1 && this.limiter-- <= 0) {
                Scheduler.this.cancel();
                return;
            }


            if (this.start == -1 && this.end == -1) {
                cons.accept(this.runnable, this.counter++);
            } else if (this.start == this.end) {
                cons.accept(this.runnable, this.counter++);
                Scheduler.this.cancel();
            } else if (this.start < this.end) {
                if (this.counter > this.end) Scheduler.this.cancel();
                if (this.counter >= this.start && this.counter <= this.end) cons.accept(this.runnable, this.counter++);
            } else {
                if (this.counter < this.end) Scheduler.this.cancel();
                if (this.counter <= this.start && this.counter >= this.end) cons.accept(this.runnable, this.counter--);
            }
        }, this.after, this.every, TimeUnit.MILLISECONDS);

        return this;
    }
}