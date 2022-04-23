package com.hakan.core.utils.query;

import javax.annotation.Nonnull;
import java.util.Objects;

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
        this.table = Objects.requireNonNull(table, "table name cannot be null!");
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