package com.dewey.core.utils;

import java.util.Map;

import static com.sun.tools.doclint.Entity.nu;

/**
 * @program dataMaker
 * @description: map工具集
 * @author: xielinzhi
 * @create: 2019/07/26 14:06
 */

public class MapUtils {

    public static String getString(Map map , String key , String defaultValue){
        if(map.get(key) == null){
            return defaultValue;
        }
        if(map.get(key) instanceof String){
            return map.get(key).toString();
        }
        return defaultValue;
    }

    public static Integer getInteger(Map map , String key , Integer defaultValue){
        if(map.get(key) == null){
            return defaultValue;
        }
        if(map.get(key) instanceof Integer){
            return (Integer) map.get(key);
        }
        return defaultValue;
    }
}
