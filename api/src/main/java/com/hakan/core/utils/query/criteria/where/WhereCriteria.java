package com.hakan.core.utils.query.criteria.where;

import com.hakan.core.utils.query.criteria.QueryCriteria;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class WhereCriteria extends QueryCriteria {
    private final Map<String, String> where;

    public WhereCriteria() {
        this.where = new LinkedHashMap<>();
    }

    public void add(@Nonnull String column, @Nonnull Object value) {
        Objects.requireNonNull(column, "column cannot be null!");
        Objects.requireNonNull(value, "value cannot be null!");

        String columnReplaced = column.replace(column, "`" + column + "`");
        String valueReplaced = value.toString().replace("'", "''");
        this.where.put(columnReplaced, valueReplaced.replace(valueReplaced, "'" + valueReplaced + "'"));
    }

    @Override
    public String getCriteriaQuery() {
        if (this.where.isEmpty()) return "";

        this.criteria.append(" WHERE ");
        where.forEach((key, value) -> criteria.append(key).append(" = ").append(value).append(" AND "));
        this.criteria.delete(this.criteria.length() - 5, this.criteria.length());
        return this.criteria.toString();
    }
}
