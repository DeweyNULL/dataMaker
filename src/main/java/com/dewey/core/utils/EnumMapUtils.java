package com.dewey.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program dataMaker
 * @description: 枚举表暂存策略
 * @author: xielinzhi
 * @create: 2019/07/24 16:24
 */
@Slf4j
public class EnumMapUtils {

    private static Map<String, List<String>> enumMap = new HashMap();

    public static List get(String colName , String colConfig){
        if(enumMap.get(colName) == null){
            synchronized (EnumMapUtils.class){
                List<String> enumList = new ArrayList<>();
                try {
                    if (enumMap.get(colName) == null){
                        String[] temp = colConfig.split(",");
                        for (String enumData : temp) {
                             String[] enumDataList = enumData.split(":");
                             int times = Integer.parseInt(enumDataList[1]);
                            for (int i = 0; i < times; i++) {
                                enumList.add(enumDataList[0]);
                            }
                        }
                        enumMap.put(colName,enumList);
                    }
                }catch (Exception e){
                    log.error("预计列配置数据出错",e);
                }
            }
        }
        return enumMap.get(colName);
    }

    public static void clearMap(){
        enumMap.clear();
    }
}
