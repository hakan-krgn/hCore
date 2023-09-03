package com.hakan.core.utils.query;

import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;

/**
 * Query builder class.
 */
public abstract class QueryBuilder {

    protected final String table;
    protected final StringBuilder query;

    /**
     * QueryBuilder constructor.
     *
     * @param table the table name.
     */
    public QueryBuilder(@Nonnull String table) {
        this.table = Validate.notNull(table, "table name cannot be null!");
        this.query = new StringBuilder();
    }

    /**
     * Builds the query.
     *
     * @return the query.
     */
    @Nonnull
    public abstract String build();

    /**
     * Builds the query.
     *
     * @return the query.
     */
    @Nonnull
    @Override
    public String toString() {
        return this.build();
    }
}
