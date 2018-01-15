package com.jlu.common.db;

import java.io.Serializable;

/**
 * Created by langshiquan on 17/10/7.
 */
public interface BaseDao<C> {
    public void save(C c);

    public void saveOrUpdate(C c);

    public void update(C c);

    public C findById(Serializable id);
}
