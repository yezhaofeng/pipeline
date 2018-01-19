package com.jlu.common.utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by langshiquan on 18/1/19.
 */
public class MapUtils {
    // 系统参数优于用户自定义参数
    public static Map<String, Object> merge(Map<String, Object> source, Map<String, Object> target) {
        if (source == null) {
            return target;
        }
        if (target == null) {
            return source;
        }
        Set<String> keySet = source.keySet();
        for (String key : keySet) {
            target.put(key, source.get(key));
        }
        return target;
    }
}
