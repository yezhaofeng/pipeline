package com.jlu.common.utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by langshiquan on 18/1/19.
 */
public class MapUtils {
    // 系统参数优于用户自定义参数,执行期参数优于配置参数
    public static Map<String, Object> merge(Map<String, Object> from, Map<String, Object> to) {
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
}
