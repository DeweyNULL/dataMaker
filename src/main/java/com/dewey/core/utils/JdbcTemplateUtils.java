package com.dewey.core.utils;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program dataMaker
 * @description: 获取jdbcTemplate的方法
 * @author: xielinzhi
 * @create: 2019/07/12 14:56
 */

public class JdbcTemplateUtils {

    private static Map<Integer,JdbcTemplate> jdbcTemplateMap = new HashMap<>();

    public static JdbcTemplate get(int i ,String url ,String userName , String password){
        if(jdbcTemplateMap.get(i) == null){
            synchronized (JdbcTemplateUtils.class){
                if(jdbcTemplateMap.get(i) == null){
                    JdbcTemplate jdbcTemplate = JdbcUtils.jdbcTemplate(url,userName,password);
                    jdbcTemplateMap.put(i,jdbcTemplate);
                    return jdbcTemplate;
                }
            }
        }
        return jdbcTemplateMap.get(i);
    }

    public static JdbcTemplate getById(int id){
        return jdbcTemplateMap.get(id);
    }
}
