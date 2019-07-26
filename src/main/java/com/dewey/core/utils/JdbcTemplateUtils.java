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

    public static JdbcTemplate get(int id ,String url ,String userName , String password){
        if(jdbcTemplateMap.get(id) == null){
            synchronized (JdbcTemplateUtils.class){
                if(jdbcTemplateMap.get(id) == null){
                    JdbcTemplate jdbcTemplate = JdbcUtils.jdbcTemplate(url,userName,password);
                    jdbcTemplateMap.put(id,jdbcTemplate);
                    return jdbcTemplate;
                }
            }
        }
        return jdbcTemplateMap.get(id);
    }

    public static JdbcTemplate getById(int id){
        return jdbcTemplateMap.get(id);
    }
}
