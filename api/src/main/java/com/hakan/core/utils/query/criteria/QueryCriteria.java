package com.hakan.core.utils.query.criteria;

public abstract class QueryCriteria {
    protected final StringBuilder criteria = new StringBuilder();
    public abstract String getCriteriaQuery();
}
