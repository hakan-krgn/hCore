package com.hakan.core.scheduler;

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
        new Thread(() -> {
            while (!Thread.interrupted()) {
                for (Scheduler scheduler : new ArrayList<>(schedulers))
                    if (scheduler.isCancelled())
                        scheduler.cancel();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private final JavaPlugin plugin;
    private final List<Function<BukkitRunnable, Boolean>> freezeFilters;
    private final List<Function<BukkitRunnable, Boolean>> terminateFilters;

    private BukkitRunnable runnable;
    private Runnable endRunnable;
    private boolean async;
    private Long limiter;

    private long after;
    private Long every;

    private long counter;
    private long start;
    private long end;


    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin.
     * @param async  Async.
     */
    public Scheduler(@Nonnull JavaPlugin plugin, boolean async) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
        this.terminateFilters = new LinkedList<>();
        this.freezeFilters = new LinkedList<>();
        this.async = async;
        this.end = Long.MAX_VALUE;
        this.start = 0;
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
        return this.runnable == null || !Bukkit.getScheduler().isCurrentlyRunning(this.getTaskId()) &&
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
        this.counter = start;
        this.start = start;
        this.end = end;
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
    public Scheduler after(long after) {
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
    public Scheduler every(long every) {
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
    public Scheduler after(long after, @Nonnull TimeUnit timeUnit) {
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
    public Scheduler every(long every, @Nonnull TimeUnit timeUnit) {
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
    public Scheduler after(@Nonnull Duration duration) {
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
    public Scheduler every(@Nonnull Duration duration) {
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
                for (Function<BukkitRunnable, Boolean> terminateFilter : terminateFilters)
                    if (terminateFilter.apply(this))
                        Scheduler.this.cancel();
                for (Function<BukkitRunnable, Boolean> freezeFilter : freezeFilters)
                    if (freezeFilter.apply(this))
                        return;

                if (Scheduler.this.isCancelled())
                    return;

                if (start > end) {
                    if (counter <= start && counter >= end) taskConsumer.accept(this, counter--);
                    if (counter < end) Scheduler.this.cancel();
                } else if (start < end) {
                    if (counter >= start && counter <= end) taskConsumer.accept(this, counter++);
                    if (counter > end) Scheduler.this.cancel();
                } else {
                    taskConsumer.accept(this, counter);
                    Scheduler.this.cancel();
                }

                if (Scheduler.this.isCancelled())
                    return;

                if (limiter != null && --limiter <= 0)
                    Scheduler.this.cancel();
            }
        };

        if (this.async) {
            if (this.every == null) this.runnable.runTaskLaterAsynchronously(this.plugin, this.after).getTaskId();
            else this.runnable.runTaskTimerAsynchronously(this.plugin, this.after, this.every).getTaskId();
        } else {
            if (this.every == null) this.runnable.runTaskLater(this.plugin, this.after).getTaskId();
            else this.runnable.runTaskTimer(this.plugin, this.after, this.every).getTaskId();
        }

        schedulers.add(this);
        return this;
    }
}