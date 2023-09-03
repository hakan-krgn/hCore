package com.hakan.core.utils.query.select;

import com.hakan.core.utils.Validate;
import com.hakan.core.utils.query.QueryBuilder;
import com.hakan.core.utils.query.criteria.order.OrderCriteria;
import com.hakan.core.utils.query.criteria.order.OrderType;
import com.hakan.core.utils.query.criteria.where.WhereCriteria;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * Query builder for
 * select query.
 */
public final class SelectQuery extends QueryBuilder {

    private final List<String> values;
    private final WhereCriteria whereCriteria;
    private final OrderCriteria orderCriteria;

    /**
     * Query builder for select query.
     *
     * @param table Table name.
     */
    public SelectQuery(@Nonnull String table) {
        super(table);
        this.values = new LinkedList<>();
        this.whereCriteria = new WhereCriteria();
        this.orderCriteria = new OrderCriteria();
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
        Validate.notNull(column, "column cannot be null!");

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
        this.whereCriteria.add(column, value);
        return this;
    }

    /**
     * Adds order by column to query.
     *
     * @param orderType Order type.
     * @param columns   Columns to order.
     * @return Query builder.
     */
    @Nonnull
    public SelectQuery orderBy(@Nonnull OrderType orderType, @Nonnull String... columns) {
        this.orderCriteria.add(orderType, columns);
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

        this.query.append(this.whereCriteria.getCriteriaQuery());
        this.query.append(this.orderCriteria.getCriteriaQuery());

        return this.query.toString();
    }
}
