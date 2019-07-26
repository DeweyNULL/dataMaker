package com.dewey.core.utils;

import java.util.List;

/**
 * @program dataMaker
 * @description: 一些写死的sql工具模板
 * @author: xielinzhi
 * @create: 2019/07/25 15:03
 */

public class SQLUtils {

    public static String getInsertString(String tableName ,String dbType, List<String> colNameList ,List<String> colTypeList,List<String> colValueList){
        String colNameArr = "(";
        String place ="";
        int size = colNameList.size();
        if(ListUtils.isNotEmpty(colNameList)){
            for (int i=0 ; i <size ;i++ ) {
                colNameArr = colNameArr+colNameList.get(i)+",";
                if(colTypeList.get(i).contains("var") || colTypeList.get(i).contains("VAR")){
                    place = place +"'"+ colValueList.get(i)+"',";
                }else if(colTypeList.get(i).contains("timest") ||colTypeList.get(i).contains("TIMEST")){
                    if(DataProjectMethodUtils.MYSQL.equals(dbType)){
                        place = place +"'"+ colValueList.get(i)+"',";
                    }

                }else {
                    place = place + colValueList.get(i)+",";
                }

            }
        }
        colNameArr = colNameArr.substring(0,colNameArr.length()-1)+")";
        place = place.substring(0,place.length()-1);
        return "insert into "+tableName + colNameArr + " values (" +place+")";
    }

    public static String getTableCountSql(String tableName){
        return "select count(1) from "+tableName;
    }
}
