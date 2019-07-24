package com.dewey.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dewey.core.mapper.DataProjectMapper;
import com.dewey.core.pojo.DataProject;
import com.dewey.core.service.DataTableService;
import com.dewey.core.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * @program dataMaker
 * @description: 数据表内容
 * @author: xielinzhi
 * @create: 2019/07/12 10:33
 */
@Service
@Slf4j
public class DataTableServiceImpl implements DataTableService {

    @Autowired
    DataProjectMapper dataProjectMapper;

    @Override
    public List<Object> getTableList(int id) {
        DataProject dataProject = dataProjectMapper.selectById(id);
        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.get(id,
                dataProject.getDatasource(), dataProject.getUsername(), dataProject.getPassword());
        List<Map<String, Object>> tabels = jdbcTemplate.queryForList(DataProjectMethodUtils.showTables(dataProject.getDatasource()));
        log.info(JSONObject.toJSONString(tabels));
        List<Object> returnList = new ArrayList<>();
        if(ListUtils.isNotEmpty(tabels)){
            for (Map<String, Object> temp: tabels) {
                returnList.addAll(temp.values());
            }
        }
        return returnList;
    }

    @Override
    public List<Map<String, Object>> getTableInfo(int id, String tableName, String type) {
        JdbcTemplate jdbcTemplate = (JdbcTemplateUtils.getById(id) == null ? getJdbcTemplate(id) : JdbcTemplateUtils.getById(id));
        Connection connection = null ;
        List<Map<String, Object>> tableMap = new ArrayList<>();
        try{
            connection = jdbcTemplate.getDataSource().getConnection();
            ResultSet rs = null;
            if(DataProjectMethodUtils.MYSQL.equals(type)){
                DataProject dataProject = dataProjectMapper.selectById(id);
                String sql="select COLUMN_NAME, COLUMN_TYPE AS 'DATA_TYPE', COLUMN_COMMENT AS 'COMMENTS', " +
                        "(CASE IS_NULLABLE WHEN 'NO' THEN 'N' WHEN 'YES' THEN 'Y' END) as 'NULLABLE', " +
                        "(CASE COLUMN_KEY WHEN 'PRI' THEN 'Y' END) as 'PRIMARY_KEY'," +
                        "(CASE  WHEN NUMERIC_PRECISION is null THEN CHARACTER_MAXIMUM_LENGTH else NUMERIC_PRECISION END) as 'LENGTH' "+
                        "from INFORMATION_SCHEMA.COLUMNS " +
                        "where TABLE_SCHEMA = ? and TABLE_NAME=?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, ToolUtils.schemaName(dataProject.getDatasource()));
                stmt.setString(2,tableName);
                rs = stmt.executeQuery();
            }else if(DataProjectMethodUtils.ORACLE.equals(type)){
                String sql="SELECT T1.COLUMN_NAME, T1.DATA_TYPE,LENGTH,T1.COMMENTS, T1.NULLABLE, (CASE T2.CONSTRAINT_TYPE WHEN 'P' THEN 'Y' END) " +
                        "AS \"PRIMARY_KEY\" FROM (SELECT T1.COLUMN_NAME, " +
                        "(case when T1.DATA_PRECISION is null then T1.DATA_LENGTH else T1.DATA_PRECISION end ) as LENGTH,"+
                        "(CASE T1.DATA_TYPE WHEN 'NUMBER' THEN CONCAT(CONCAT('NUMBER(', T1.DATA_PRECISION), ')') " +
                        "WHEN 'VARCHAR2' THEN CONCAT(CONCAT('VARCHAR(', T1.DATA_LENGTH), ')') " +
                        "ELSE T1.DATA_TYPE END) DATA_TYPE, T1.NULLABLE, T2.COMMENTS " +
                        "FROM USER_TAB_COLUMNS T1, USER_COL_COMMENTS T2 " +
                        "WHERE T1.TABLE_NAME = T2.TABLE_NAME AND T1.COLUMN_NAME = T2.COLUMN_NAME " +
                        "AND T1.TABLE_NAME = ? " +
                        "ORDER BY COLUMN_ID) T1 LEFT JOIN (SELECT T1.COLUMN_NAME, T2.CONSTRAINT_TYPE " +
                        "FROM USER_CONS_COLUMNS T1, USER_CONSTRAINTS T2 " +
                        "WHERE T1.CONSTRAINT_NAME = T2.CONSTRAINT_NAME AND T1.TABLE_NAME = ?" +
                        "AND T2.CONSTRAINT_TYPE = 'P') T2 ON T1.COLUMN_NAME = T2.COLUMN_NAME";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,tableName);
                stmt.setString(2,tableName);
                rs = stmt.executeQuery();
            }

            while (rs.next()){
                Map<String,Object> map = new HashMap();
                map.put("name", rs.getString("COLUMN_NAME"));
                map.put("type", rs.getString("DATA_TYPE"));
                map.put("comments", rs.getString("COMMENTS"));
                map.put("nullAble",rs.getString("NULLABLE"));
                map.put("size", rs.getInt("LENGTH"));
                if("Y".equals( rs.getString("PRIMARY_KEY"))){
                    map.put("isPrimary",true);
                }
                tableMap.add(map);
            }
        }
        catch (Exception e){
            log.error("查询表信息出错",e);
        }finally {
            try {
                if(connection!=null){
                    connection.close();
                }
            }catch (Exception e){
                log.error("关闭连接错误",e);
            }
        }
        return tableMap;
    }


    JdbcTemplate getJdbcTemplate(int id){
        DataProject dataProject = dataProjectMapper.selectById(id);
        return JdbcTemplateUtils.get(id,
                dataProject.getDatasource(), dataProject.getUsername(), dataProject.getPassword());
    }

}
