package com.jlu.common.utils.bean;

/**
 * Created by langshiquan on 18/1/22.
 */
public abstract class AbstractPropertyGetter<K, T> extends AbstractBeanProcessor<K, T> {
    @Override
    public final K process(T t) {
        return get(t);
    }

    public abstract K get(T t);
}
