package com.dewey.core.utils;

import java.util.List;

/**
 * @program dataMaker
 * @description: list工具
 * @author: xielinzhi
 * @create: 2019/07/12 14:33
 */

public class ListUtils {

    public static boolean isEmpty(List list){
        if(list == null || list.size()<1){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(List list){
        return !isEmpty(list);
    }
}
