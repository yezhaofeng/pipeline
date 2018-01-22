package com.jlu.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.jlu.common.utils.bean.AbstractBeanProcessor;

/**
 * Created by langshiquan on 18/1/22.
 */
public class ListUtils extends CollectionUtils {
    /**
     * 集合批量处理
     *
     * @param collection 集合
     * @param processor  元素处理，if null return empty
     *
     * @return 批量结果 @NotNull
     */
    public static <K, T> List<K> toList(Collection<T> collection,
                                        AbstractBeanProcessor<K, T> processor) {
        List<K> list = new ArrayList<K>();
        if (isEmpty(collection) || null == processor) {
            return list;
        }
        for (T t : collection) {
            list.add(processor.process(t));
        }
        return list;
    }

    /**
     * 集合批量处理并去重
     *
     * @param collection 集合
     * @param processor  元素处理，if null return empty
     *
     * @return 批量结果@NotNull
     */
    public static <K, T> Set<K> toSet(Collection<T> collection,
                                      AbstractBeanProcessor<K, T> processor) {
        Set<K> set = new HashSet<K>();
        if (isEmpty(collection) || null == processor) {
            return set;
        }
        for (T t : collection) {
            set.add(processor.process(t));
        }
        return set;
    }

    /**
     * 集合批量处理
     *
     * @param collection 集合
     * @param processor  元素处理，if null return empty
     *
     * @return 批量结果@NotNull
     */
    public static <K, T> Map<K, T> toMap(Collection<T> collection,
                                         AbstractBeanProcessor<K, T> processor) {
        Map<K, T> map = new HashMap<K, T>();
        if (isEmpty(collection) || null == processor) {
            return map;
        }
        for (T t : collection) {
            map.put(processor.process(t), t);
        }
        return map;
    }

    /**
     * 集合批量处理
     *
     * @param collection 集合
     * @param kp         由元素生成map.key，if null return empty
     * @param vp         由元素生成map.value，if null return empty
     *
     * @return 批量结果@NotNull
     */
    public static <K, V, T> Map<K, V> toMap(Collection<T> collection,
                                            AbstractBeanProcessor<K, T> kp, AbstractBeanProcessor<V, T> vp) {
        Map<K, V> map = new HashMap<K, V>();
        if (isEmpty(collection) || null == kp || null == vp) {
            return map;
        }
        for (T t : collection) {
            map.put(kp.process(t), vp.process(t));
        }
        return map;
    }
}
