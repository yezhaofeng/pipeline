package com.jlu.common.utils.bean;

/**
 * bean加工器：属性提取整合等
 *
 * @param K 结果类型
 * @param T bean类型
 *
 */
public abstract class AbstractBeanProcessor<K, T> {

    public abstract K process(T t);

}
