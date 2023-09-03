package com.hakan.core.scheduler;

import com.hakan.core.utils.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

/**
 * Replication of BukkitRunnable
 * class to handle some events.
 */
public final class SchedulerRunnable extends BukkitRunnable {

    private final JavaPlugin plugin;

    private Runnable endRunnable;
    private Runnable startRunnable;
    private Runnable processRunnable;

    /**
     * Creates new instance of this class.
     *
     * @param plugin Plugin.
     */
    public SchedulerRunnable(@Nonnull JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin cannot be null!");
    }

    /**
     * Gets the task id.
     *
     * @return Task id.
     */
    public int getId() {
        return super.getTaskId();
    }

    /**
     * Checks if scheduler is
     * cancelled or not.
     *
     * @return Cancelled.
     */
    @Override
    public boolean isCancelled() {
        return super.isCancelled();
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
        super.runTaskLater(this.plugin, delay);
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
        super.runTaskTimer(this.plugin, delay, period);
    }

    /**
     * Runs the bukkit runnable
     * after the given delay asynchronously.
     *
     * @param delay Delay.
     */
    public void runAsyncLater(long delay) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        super.runTaskLaterAsynchronously(this.plugin, delay);
    }

    /**
     * Runs the bukkit runnable
     * after the given delay and
     * repeats it every given period asynchronously.
     *
     * @param delay Delay.
     */
    public void runAsyncTimer(long delay, long period) {
        if (this.startRunnable != null)
            this.startRunnable.run();
        super.runTaskTimerAsynchronously(this.plugin, delay, period);
    }

    /**
     * Cancels the scheduler.
     */
    @Override
    public void cancel() {
        if (this.endRunnable != null)
            this.endRunnable.run();
        super.cancel();
    }

    /**
     * Runs the bukkit runnable.
     */
    @Override
    public void run() {
        if (this.processRunnable != null)
            this.processRunnable.run();
    }
}
