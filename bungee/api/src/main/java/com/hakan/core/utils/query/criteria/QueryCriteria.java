package com.hakan.core.utils.query.criteria;

/**
 * Query criteria interface.
 */
public abstract class QueryCriteria {

    protected final StringBuilder criteria = new StringBuilder();

    /**
     * Builds criteria query.
     *
     * @return Criteria query.
     */
    public abstract String getCriteriaQuery();

}
