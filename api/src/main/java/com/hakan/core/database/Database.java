package com.hakan.core.database;

/**
 * Database class to implement
 * databases.
 *
 * @param <T> Database object class.
 */
public abstract class Database<T extends DatabaseObject> {

    protected T t;

    /**
     * Creates new instance of this class.
     *
     * @param t Parent class.
     */
    public Database(T t) {
        this.t = t;
    }

    /**
     * Gets parent class.
     *
     * @return Parent class.
     */
    public T getParent() {
        return this.t;
    }

    /**
     * Table create, yaml create, etc.
     */
    public abstract void create();


    /**
     * Inserts data to database.
     */
    public abstract void insert();

    /**
     * Saves data to database.
     */
    public abstract void save();

    /**
     * Deletes data from database.
     */
    public abstract void delete();
}