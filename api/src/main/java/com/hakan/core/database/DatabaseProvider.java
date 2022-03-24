package com.hakan.core.database;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Database class to implement
 * databases.
 *
 * @param <T> Database object class.
 */
@SuppressWarnings({"unused"})
public interface DatabaseProvider<T extends DatabaseObject> {

    /**
     * Table create, yaml create, etc.
     */
    void create();

    /**
     * Gets values.
     *
     * @return Values.
     */
    @Nonnull
    List<T> getValues();

    /**
     * Inserts data to database.
     *
     * @param t T
     */
    void insert(@Nonnull T t);

    /**
     * Saves data to database.
     *
     * @param t T
     */
    void update(@Nonnull T t);

    /**
     * Deletes data from database.
     *
     * @param t T
     */
    void delete(@Nonnull T t);

    /**
     * Inserts data to database.
     *
     * @param t T
     */
    void insert(@Nonnull Collection<T> t);

    /**
     * Saves data to database.
     *
     * @param t T
     */
    void update(@Nonnull Collection<T> t);

    /**
     * Deletes data from database.
     *
     * @param t T
     */
    void delete(@Nonnull Collection<T> t);
}