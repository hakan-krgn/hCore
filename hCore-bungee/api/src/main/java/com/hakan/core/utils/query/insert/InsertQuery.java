package com.hakan.core.utils.query.insert;

import com.hakan.core.utils.Validate;
import com.hakan.core.utils.query.QueryBuilder;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Query builder for
 * insert query.
 */
public final class InsertQuery extends QueryBuilder {

    private final Map<String, String> values;

    /**
     * Query builder for insert query.
     *
     * @param table table name.
     */
    public InsertQuery(@Nonnull String table) {
        super(table);
        this.values = new HashMap<>();
    }

    /**
     * Adds value to insert query.
     *
     * @param column Column name.
     * @param value  Value.
     * @return This.
     */
    @Nonnull
    public InsertQuery value(@Nonnull String column, @Nonnull Object value) {
        Validate.notNull(column, "column name cannot be null!");
        Validate.notNull(value, "value name cannot be null!");

        String columnReplaced = column.replace(column, "`" + column + "`");
        String valueReplaced = value.toString().replace("'", "''");
        this.values.put(columnReplaced, valueReplaced.replace(valueReplaced, "'" + valueReplaced + "'"));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String build() {
        this.query.append("INSERT INTO ").append(this.table);
        this.query.append(" (");
        this.query.append(String.join(", ", this.values.keySet()));
        this.query.append(") VALUES (");
        this.query.append(String.join(", ", this.values.values()));
        this.query.append(")");
        return this.query.toString();
    }
}
