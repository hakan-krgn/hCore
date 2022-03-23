package com.hakan.core.database;

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
     * Gets values.
     *
     * @return Values.
     */
    List<T> getValues();

    /**
     * Table create, yaml create, etc.
     */
    void create();

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
}