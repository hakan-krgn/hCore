package com.hakan.core.utils.query.create;

import com.hakan.core.utils.Validate;
import com.hakan.core.utils.query.QueryBuilder;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query builder for
 * create table query.
 */
public final class CreateQuery extends QueryBuilder {

    private final Map<String, String> values;

    /**
     * Creates a new query builder.
     *
     * @param table the table name.
     */
    public CreateQuery(@Nonnull String table) {
        super(table);
        this.values = new LinkedHashMap<>();
    }

    /**
     * Adds a column to the query.
     *
     * @param column the column name.
     * @param type   the column type.
     * @return the query builder.
     */
    @Nonnull
    public CreateQuery value(@Nonnull String column, @Nonnull String type) {
        Validate.notNull(column, "column name cannot be null!");
        Validate.notNull(type, "type name cannot be null!");

        this.values.put(column, type);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String build() {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        query.append(this.table);
        query.append("(");
        this.values.forEach((key, value) -> {
            query.append(key);
            query.append(" ");
            query.append(value);
            query.append(", ");
        });
        query.delete(query.length() - 2, query.length());
        query.append(")");
        return query.toString();
    }
}
