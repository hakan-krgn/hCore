package com.hakan.core.utils.query.criteria.order;


import com.hakan.core.utils.query.criteria.QueryCriteria;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OrderCriteria extends QueryCriteria {
    private final Map<String, OrderType> orderBy;

    public OrderCriteria() {
        this.orderBy = new LinkedHashMap<>();
    }

    public void add(@Nonnull OrderType orderType, @Nonnull String... columns) {
        Objects.requireNonNull(orderType, "orderType cannot be null!");
        Objects.requireNonNull(columns, "columns cannot be null!");

        for (String column : columns) {
            this.orderBy.put(column, orderType);
        }
    }

    @Override
    public String getCriteriaQuery() {
        if (this.orderBy.isEmpty()) return "";

        this.criteria.append(" ORDER BY ");
        orderBy.forEach((key, value) -> this.criteria.append(key).append(" ").append(value.name()).append(", "));
        this.criteria.delete(this.criteria.length() - 2, this.criteria.length());
        return this.criteria.toString();
    }
}
