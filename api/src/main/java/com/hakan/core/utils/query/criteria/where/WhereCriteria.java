package com.hakan.core.utils.query.criteria.where;

import com.hakan.core.utils.query.criteria.QueryCriteria;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Where criteria class.
 */
public final class WhereCriteria extends QueryCriteria {

    private final Map<String, String> where;

    /**
     * Creates new instance of WhereCriteria class.
     */
    public WhereCriteria() {
        this.where = new LinkedHashMap<>();
    }

    /**
     * Adds new where criteria.
     *
     * @param column Column name.
     * @param value  Value.
     * @return Where criteria.
     */
    @Nonnull
    public WhereCriteria add(@Nonnull String column, @Nonnull Object value) {
        Objects.requireNonNull(column, "column cannot be null!");
        Objects.requireNonNull(value, "value cannot be null!");

        String columnReplaced = column.replace(column, "`" + column + "`");
        String valueReplaced = value.toString().replace("'", "''");
        this.where.put(columnReplaced, valueReplaced.replace(valueReplaced, "'" + valueReplaced + "'"));
        return this;
    }

    /**
     * Builds where criteria.
     *
     * @return Where criteria.
     */
    @Nonnull
    @Override
    public String getCriteriaQuery() {
        if (this.where.isEmpty()) return "";

        this.criteria.append(" WHERE ");
        this.where.forEach((key, value) -> criteria.append(key).append(" = ").append(value).append(" AND "));
        this.criteria.delete(this.criteria.length() - 5, this.criteria.length());

        return this.criteria.toString();
    }
}