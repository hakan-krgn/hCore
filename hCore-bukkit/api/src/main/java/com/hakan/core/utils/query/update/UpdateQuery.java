package com.hakan.core.utils.query.update;

import com.hakan.core.utils.Validate;
import com.hakan.core.utils.query.QueryBuilder;
import com.hakan.core.utils.query.criteria.where.WhereCriteria;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query builder for
 * update query.
 */
public final class UpdateQuery extends QueryBuilder {

    private final Map<String, String> values;
    private final WhereCriteria whereCriteria;

    /**
     * Query builder for update query.
     *
     * @param table Table name.
     */
    public UpdateQuery(@Nonnull String table) {
        super(table);
        this.values = new LinkedHashMap<>();
        this.whereCriteria = new WhereCriteria();
    }

    /**
     * Adds value to update query.
     *
     * @param column Column name.
     * @param value  Value.
     * @return Update query.
     */
    @Nonnull
    public UpdateQuery value(@Nonnull String column, @Nonnull Object value) {
        Validate.notNull(column, "column cannot be null!");
        Validate.notNull(value, "value cannot be null!");

        String columnReplaced = column.replace(column, "`" + column + "`");
        String valueReplaced = value.toString().replace("'", "''");
        this.values.put(columnReplaced, valueReplaced.replace(valueReplaced, "'" + valueReplaced + "'"));
        return this;
    }

    /**
     * Adds where condition to update query.
     *
     * @param column Column name.
     * @param value  Value.
     * @return Update query.
     */
    @Nonnull
    public UpdateQuery where(@Nonnull String column, @Nonnull Object value) {
        this.whereCriteria.add(column, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String build() {
        this.query.append("UPDATE ").append(this.table).append(" SET ");
        this.values.forEach((k, v) -> this.query.append(k).append(" = ").append(v).append(", "));
        this.query.delete(this.query.length() - 2, this.query.length());

        this.query.append(this.whereCriteria.getCriteriaQuery());

        return this.query.toString();
    }
}
