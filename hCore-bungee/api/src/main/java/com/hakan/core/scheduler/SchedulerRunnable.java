package com.hakan.core.scheduler;

import com.hakan.core.utils.Validate;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * Replication of BukkitRunnable
 * class to handle some events.
 */
public final class SchedulerRunnable implements ScheduledTask {

    private static int globalId = Integer.MAX_VALUE;



    private final Plugin plugin;
    private final TaskScheduler taskScheduler;

    private final int id;
    private boolean cancelled;

    private Runnable endRunnable;
    private Runnable startRunnable;
    private Runnable processRunnable;

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin.
     */
    public SchedulerRunnable(@Nonnull Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
        this.id = globalId--;
        this.cancelled = false;
        this.taskScheduler = plugin.getProxy().getScheduler();
    }

    /**
     * Gets the task id.
     *
     * @return Task id.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Gets the owner plugin.
     *
     * @return Owner plugin.
     */
    @Override
    public Plugin getOwner() {
        return this.plugin;
    }

    /**
     * Gets the task.
     *
     * @return Task.
     */
    @Override
    public Runnable getTask() {
        return this.processRunnable;
    }

    /**
     * Checks if scheduler is
     * cancelled or not.
     *
     * @return Cancelled.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Runs when the
     * scheduler is started.
     *
     * @param runnable Runnable.
     */
    @Nonnull
    public SchedulerRunnable whenStarted(@Nonnull Runnable runnable) {
        this.startRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    /**
     * Runs when the
     * scheduler is processed.
     *
     * @param runnable Runnable.
     */
    @Nonnull
    public SchedulerRunnable whenProcessed(@Nonnull Runnable runnable) {
        this.processRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    /**
     * Runs when the
     * scheduler is ended.
     *
     * @param runnable Runnable.
     */
    @Nonnull
    public SchedulerRunnable whenEnded(@Nonnull Runnable runnable) {
        this.endRunnable = Validate.notNull(runnable, "runnable cannot be null!");
        return this;
    }

    /**
     * Runs the bukkit runnable
     * after the given delay.
     *
     * @param delay Delay.
     */
    public void runLater(long delay) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        if (this.processRunnable != null)
            this.taskScheduler.schedule(this.plugin, this.processRunnable, delay * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Runs the bukkit runnable
     * after the given delay and
     * repeats it every given period.
     *
     * @param delay Delay.
     */
    public void runTimer(long delay, long period) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        if (this.processRunnable != null)
            this.taskScheduler.schedule(this.plugin, this.processRunnable, delay * 50, period * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Runs the bukkit runnable
     * after the given delay asynchronously.
     *
     * @param delay Delay.
     */
    public void runAsyncLater(long delay) {
        this.taskScheduler.runAsync(this.plugin, () -> this.runLater(delay));
    }

    /**
     * Runs the bukkit runnable
     * after the given delay and
     * repeats it every given period asynchronously.
     *
     * @param delay Delay.
     */
    public void runAsyncTimer(long delay, long period) {
        this.taskScheduler.runAsync(this.plugin, () -> this.runTimer(delay, period));
    }

    /**
     * Cancels the scheduler.
     */
    @Override
    public void cancel() {
        if (this.endRunnable != null)
            this.endRunnable.run();

        this.cancelled = true;
        this.taskScheduler.cancel(this);
    }
}
