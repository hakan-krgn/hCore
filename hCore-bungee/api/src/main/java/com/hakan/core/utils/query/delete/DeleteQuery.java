package com.hakan.core.utils.query.delete;

import com.hakan.core.utils.query.QueryBuilder;
import com.hakan.core.utils.query.criteria.where.WhereCriteria;

import javax.annotation.Nonnull;

/**
 * Query builder for
 * delete query.
 */
public final class DeleteQuery extends QueryBuilder {

    private final WhereCriteria whereCriteria;

    /**
     * Query builder for delete query.
     *
     * @param table table name
     */
    public DeleteQuery(@Nonnull String table) {
        super(table);
        this.whereCriteria = new WhereCriteria();
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
        this.whereCriteria.add(column, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String build() {
        this.query.append("DELETE FROM ").append(this.table);
        this.query.append(this.whereCriteria.getCriteriaQuery());

        return this.query.toString();
    }
}
