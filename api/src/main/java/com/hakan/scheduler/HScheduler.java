package com.hakan.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class HScheduler {

    private final JavaPlugin plugin;
    private boolean async;
    private long after;
    private Long every;

    public HScheduler(JavaPlugin plugin, boolean async) {
        this.plugin = plugin;
        this.async = async;
    }

    public boolean isAsync() {
        return this.async;
    }

    public HScheduler async(boolean async) {
        this.async = async;
        return this;
    }

    public HScheduler after(long after) {
        this.after = after;
        return this;
    }

    public HScheduler every(long every) {
        this.every = every;
        return this;
    }

    public synchronized HScheduler run(Runnable runnable) {
        return this.run(consumer -> runnable.run());
    }

    public synchronized HScheduler run(Consumer<BukkitRunnable> taskConsumer) {
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                taskConsumer.accept(this);
            }
        };

        if (this.async) {
            if (this.every == null) bukkitRunnable.runTaskLaterAsynchronously(this.plugin, this.after);
            else bukkitRunnable.runTaskTimerAsynchronously(this.plugin, this.after, this.every);
        } else {
            if (this.every == null) bukkitRunnable.runTaskLater(this.plugin, this.after);
            else bukkitRunnable.runTaskTimer(this.plugin, this.after, this.every);
        }

        return this;
    }
}