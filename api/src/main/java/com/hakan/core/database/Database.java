package com.hakan.core.database;

public abstract class Database<T extends DatabaseObject> {

    protected T t;

    public Database(T t) {
        this.t = t;
    }

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