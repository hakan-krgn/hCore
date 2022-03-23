package com.hakan.core.database;

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
    List<T> getValues();

    /**
     * Inserts data to database.
     *
     * @param t T
     */
    void insert(T t);

    /**
     * Saves data to database.
     *
     * @param t T
     */
    void save(T t);

    /**
     * Deletes data from database.
     *
     * @param t T
     */
    void delete(T t);

    /**
     * Inserts data to database.
     *
     * @param t T
     */
    void insert(Collection<T> t);

    /**
     * Saves data to database.
     *
     * @param t T
     */
    void save(Collection<T> t);

    /**
     * Deletes data from database.
     *
     * @param t T
     */
    void delete(Collection<T> t);
}