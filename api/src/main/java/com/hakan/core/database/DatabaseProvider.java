package com.hakan.core.database;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Database class to implement
 * databases.
 *
 * @param <T> Database object class.
 */
@SuppressWarnings({"unused"})
public abstract class DatabaseProvider<T extends DatabaseObject> {

    private final DatabaseUpdater<T> updater;

    /**
     * Database provider constructor.
     */
    public DatabaseProvider() {
        this.updater = new DatabaseUpdater<>(this);
    }

    /**
     * Gets database updater.
     *
     * @return Database updater.
     */
    @Nonnull
    public final DatabaseUpdater<T> getUpdater() {
        return this.updater;
    }

    /**
     * Updates all objects in the database
     * every selected time.
     *
     * @param async If true, updates will be done in a separate thread.
     * @param time  Time to wait between updates.
     * @param unit  Time unit.
     * @return Instance of this class.
     */
    public final DatabaseProvider<T> updateEvery(boolean async, int time, @Nonnull TimeUnit unit) {
        this.updater.updateEvery(async, time, unit);
        return this;
    }

    /**
     * Updates all objects in the database
     * every selected time.
     *
     * @param time Time to wait between updates.
     * @param unit Time unit.
     * @return Instance of this class.
     */
    public final DatabaseProvider<T> updateEvery(int time, @Nonnull TimeUnit unit) {
        this.updater.updateEvery(time, unit);
        return this;
    }

    /**
     * Adds an object to the database for updating.
     *
     * @param t Object to add.
     * @return Instance of this class.
     */
    public final DatabaseProvider<T> addUpdateObject(T t) {
        this.updater.add(t);
        return this;
    }

    /**
     * Removes an object from the database for updating.
     *
     * @param t Object to remove.
     * @return Instance of this class.
     */
    public final DatabaseProvider<T> removeUpdateObject(T t) {
        this.updater.remove(t);
        return this;
    }


    /**
     * Table create, yaml create, etc.
     */
    public abstract void create();

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    public abstract List<T> getValues();

    /**
     * Gets value from key and value.
     *
     * @param key   Key.
     * @param value Value.
     * @return Value.
     */
    @Nullable
    public abstract T getValue(@Nonnull String key, @Nonnull Object value);

    /**
     * Inserts data to database.
     *
     * @param t Database object.
     */
    public abstract void insert(@Nonnull T t);

    /**
     * Saves data to database.
     *
     * @param t Database object.
     */
    public abstract void update(@Nonnull T t);

    /**
     * Deletes data from database.
     *
     * @param t Database object.
     */
    public abstract void delete(@Nonnull T t);

    /**
     * Inserts data to database.
     *
     * @param t Database object.
     */
    public abstract void insert(@Nonnull Collection<T> t);

    /**
     * Saves data to database.
     *
     * @param t Database object.
     */
    public abstract void update(@Nonnull Collection<T> t);

    /**
     * Deletes data from database.
     *
     * @param t Database object.
     */
    public abstract void delete(@Nonnull Collection<T> t);
}