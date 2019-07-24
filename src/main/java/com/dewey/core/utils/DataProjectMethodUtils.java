package com.dewey.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @program dataMaker
 * @description:
 * @author: xielinzhi
 * @create: 2019/07/12 10:53
 */

@Slf4j
public class DataProjectMethodUtils {

    /**
     *  获取数据库表名
     */
    public static final String mysqlShowTables = "show tables";

    public static final String oracleShowTables = "select table_name from user_tables";
    /**
     * The constant MYSQL.
     */
    public static final String MYSQL = "mysql";
    /**
     * The constant ORACLE.
     */
    public static final String ORACLE = "oracle";

    public static final String MYSQL_PREFIX = "mysql";

    public static String showTables(String url) {
        String r = "";
        if (!StringUtils.isEmpty(url)) {
            if (url.contains(MYSQL)){
                r = mysqlShowTables;
            }else if(url.contains(ORACLE)){
                r = oracleShowTables;
            }
        }
        return r;
    }

    public static String getTabelSql(String type , String tableName){
        String r = "";
        if(MYSQL.equals(type)){
            r = MYSQL_PREFIX + tableName ;
        }else  if(ORACLE.equals(type)){

        }
        return r;
    }
}
