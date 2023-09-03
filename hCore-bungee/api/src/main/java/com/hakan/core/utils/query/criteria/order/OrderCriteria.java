package com.hakan.core.utils.query.criteria.order;

import com.hakan.core.utils.Validate;
import com.hakan.core.utils.query.criteria.QueryCriteria;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Order criteria class.
 */
public final class OrderCriteria extends QueryCriteria {

    private final Map<String, OrderType> orderBy;

    /**
     * Creates new instance of OrderCriteria.
     */
    public OrderCriteria() {
        this.orderBy = new LinkedHashMap<>();
    }

    /**
     * Adds order by order type.
     *
     * @param orderType Order type.
     * @param columns   Columns.
     * @return Order criteria.
     */
    @Nonnull
    public OrderCriteria add(@Nonnull OrderType orderType, @Nonnull String... columns) {
        Validate.notNull(orderType, "orderType cannot be null!");
        Validate.notNull(columns, "columns cannot be null!");

        Arrays.asList(columns).forEach(column -> this.orderBy.put(column, orderType));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getCriteriaQuery() {
        if (this.orderBy.isEmpty()) return "";

        this.criteria.append(" ORDER BY ");
        this.orderBy.forEach((key, value) -> this.criteria.append(key).append(" ").append(value.name()).append(", "));
        this.criteria.delete(this.criteria.length() - 2, this.criteria.length());

        return this.criteria.toString();
    }
}
