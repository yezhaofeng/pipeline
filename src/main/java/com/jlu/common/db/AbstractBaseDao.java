package com.jlu.common.db;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by langshiquan on 17/10/7.
 */

public abstract class AbstractBaseDao<C> implements BaseDao<C> {

    private SessionFactory sessionFactory;

    private Class<C> entityClass;

    public AbstractBaseDao() {
        // 通过范型反射，获取在子类中定义的entityClass.
        this.entityClass =
                (Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        // hibernate4必须配置为开启事务 否则 getCurrentSession()获取不到
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void save(C c) {
        getSession().save(c);
    }

    public C findById(Serializable id) {
        return (C) getSession().get(entityClass, id);
    }

    @Transactional
    public void saveOrUpdate(C c) {
        getSession().saveOrUpdate(c);
    }

    @Transactional
    public void update(C c) {
        getSession().update(c);
    }

    @Transactional
    public void delete(C c) {
        getSession().delete(c);
    }

}
