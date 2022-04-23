package com.hakan.core.utils.query.select;

import com.hakan.core.utils.query.QueryBuilder;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Query builder for
 * select query.
 */
public final class SelectQuery extends QueryBuilder {

    private final List<String> values;
    private final Map<String, String> where;

    /**
     * Query builder for select query.
     *
     * @param table Table name.
     */
    public SelectQuery(@Nonnull String table) {
        super(table);
        this.values = new LinkedList<>();
        this.where = new LinkedHashMap<>();
    }

    /**
     * Adds selection from everywhere to query.
     *
     * @return Query builder.
     */
    @Nonnull
    public SelectQuery fromAll() {
        this.values.clear();
        this.values.add("*");
        return this;
    }

    /**
     * Adds selection from column to query.
     *
     * @param column Column name.
     * @return Query builder.
     */
    @Nonnull
    public SelectQuery from(@Nonnull String column) {
        Objects.requireNonNull(column, "column cannot be null!");

        String columnReplaced = column.replace(column, "`" + column + "`");
        this.values.add(columnReplaced);
        return this;
    }

    /**
     * Adds where condition to query.
     *
     * @param column Column name.
     * @param value  Value.
     * @return Query builder.
     */
    @Nonnull
    public SelectQuery where(@Nonnull String column, @Nonnull Object value) {
        Objects.requireNonNull(column, "column cannot be null!");
        Objects.requireNonNull(value, "value cannot be null!");

        String columnReplaced = column.replace(column, "`" + column + "`");
        String valueReplaced = value.toString().replace("'", "''");
        this.where.put(columnReplaced, valueReplaced.replace(valueReplaced, "'" + valueReplaced + "'"));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String build() {
        this.query.append("SELECT ");
        this.values.forEach((key) -> this.query.append(key).append(", "));
        this.query.delete(this.query.length() - 2, this.query.length());
        this.query.append(" FROM ").append(this.table);
        if (this.where.size() > 0) {
            this.query.append(" WHERE ");
            this.where.forEach((key, value) -> this.query.append(key).append(" = ").append(value).append(" AND "));
            this.query.delete(this.query.length() - 5, this.query.length());
        }
        return this.query.toString();
    }
}