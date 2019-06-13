package com.jlu.common.db.sqlcondition;

/**
 * Created by Wonpang New on 2019/9/6.
 */
public class BetweenCondition extends CompareCondition {
    /**
     * Creates a new BetweenCondition object.
     *
     * @param propertyName DOCUMENT ME!
     * @param minValue DOCUMENT ME!
     * @param maxValue DOCUMENT ME!
     */
    public BetweenCondition(String propertyName, Object minValue, Object maxValue) {
        super(propertyName, minValue, maxValue);
    }
}
