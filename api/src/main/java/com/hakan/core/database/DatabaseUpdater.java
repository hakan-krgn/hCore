package com.hakan.core.database;

import com.hakan.core.HCore;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * DatabaseObject class to add and remove
 * database objects that needs to update.
 *
 * @param <T> DatabaseObject type.
 */
public final class DatabaseUpdater<T extends DatabaseObject> {

    private final DatabaseProvider<T> provider;
    private final Set<T> needUpdates;
    private int taskId;

    /**
     * Creates a new DatabaseUpdater instance.
     *
     * @param provider The provider to use.
     */
    public DatabaseUpdater(@Nonnull DatabaseProvider<T> provider) {
        this.provider = Objects.requireNonNull(provider, "provider cannot be null!");
        this.needUpdates = new HashSet<>();
    }

    /**
     * Updates all objects in the database
     * every selected time.
     *
     * @param async If true, updates will be done in a separate thread.
     * @param time  Time to wait between updates.
     * @param unit  Time unit.
     */
    public void updateEvery(boolean async, int time, @Nonnull TimeUnit unit) {
        Objects.requireNonNull(unit, "unit cannot be null!");

        if (this.taskId != -1)
            Bukkit.getScheduler().cancelTask(this.taskId);

        this.taskId = HCore.scheduler(async)
                .after(time, unit)
                .every(time, unit)
                .run(this::updateAll);
    }

    /**
     * Updates all objects in the database
     * every selected time.
     *
     * @param time Time to wait between updates.
     * @param unit Time unit.
     */
    public void updateEvery(int time, @Nonnull TimeUnit unit) {
        this.updateEvery(true, time, unit);
    }

    /**
     * Gets the contents of the update list as safe.
     *
     * @return The contents of the update list as safe.
     */
    @Nonnull
    public Set<T> getContentSafe() {
        return new HashSet<>(this.needUpdates);
    }

    /**
     * Gets the content of the update list.
     *
     * @return The content of the update list.
     */
    @Nonnull
    public Set<T> getContent() {
        return new HashSet<>(this.needUpdates);
    }

    /**
     * Clears the content of the updater.
     */
    public void clear() {
        this.needUpdates.clear();
    }

    /**
     * Adds the given object to the update list.
     *
     * @param t The object to add.
     */
    public void add(@Nonnull T t) {
        this.needUpdates.add(Objects.requireNonNull(t, "object cannot be null!"));
    }

    /**
     * Removes the given object from the need update set.
     *
     * @param t The object to remove.
     */
    public void remove(@Nonnull T t) {
        this.needUpdates.add(Objects.requireNonNull(t, "object cannot be null!"));
    }

    /**
     * Updates all needed objects
     */
    public void updateAll() {
        Set<T> needs = this.getContentSafe();
        this.needUpdates.clear();
        this.provider.update(needs);
    }
}