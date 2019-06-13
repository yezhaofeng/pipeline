package com.jlu.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.jlu.common.utils.bean.AbstractBeanProcessor;

/**
 * Created by yezhaofeng on 2019/1/22.
 */
public class CollUtils extends CollectionUtils {
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

    public static Map<String, String> merge(Map<String, String> from, Map<String, String> to) {
        if (from == null) {
            return to;
        }
        if (to == null) {
            return from;
        }
        Set<String> keySet = from.keySet();
        for (String key : keySet) {
            to.put(key, from.get(key));
        }
        return to;
    }

    /**
     * 排列组合
     *
     * @param beginArray
     * @param endArray
     *
     * @return
     */
    public static Set<String> combination(String[] beginArray, String[] endArray) {
        Set<String> combinationSet = new HashSet<>();
        if ((beginArray == null || beginArray.length == 0) && (endArray == null || endArray.length == 0)) {
            return combinationSet;
        }
        if (beginArray == null || beginArray.length == 0) {
            return new HashSet<>(Arrays.asList(endArray));
        }
        if (endArray == null || endArray.length == 0) {
            return new HashSet<>(Arrays.asList(beginArray));
        }

        for (String begin : beginArray) {
            for (String end : endArray) {
                combinationSet.add(begin + end);
            }
        }

        return combinationSet;
    }
}
