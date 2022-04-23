package com.hakan.core.utils.query.delete;

import com.hakan.core.utils.query.QueryBuilder;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Query builder for
 * delete query.
 */
public final class DeleteQuery extends QueryBuilder {

    private final Map<String, String> where;

    /**
     * Query builder for delete query.
     *
     * @param table table name
     */
    public DeleteQuery(@Nonnull String table) {
        super(table);
        this.where = new LinkedHashMap<>();
    }

    /**
     * Adds where condition.
     *
     * @param column Column name.
     * @param value  Value.
     * @return This.
     */
    @Nonnull
    public DeleteQuery where(@Nonnull String column, @Nonnull Object value) {
        Objects.requireNonNull(column, "column name cannot be null!");
        Objects.requireNonNull(value, "value name cannot be null!");

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
        this.query.append("DELETE FROM ").append(this.table);
        if (this.where.size() > 0) {
            this.query.append(" WHERE ");
            this.where.forEach((key, value) -> this.query.append(key).append(" = ").append(value).append(" AND "));
            this.query.delete(this.query.length() - 5, this.query.length());
        }
        return this.query.toString();
    }
}