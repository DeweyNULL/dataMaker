package com.dewey.core.service;

import java.util.List;
import java.util.Map;

/**
 * @program dataMaker
 * @description: 数据表内容
 * @author: xielinzhi
 * @create: 2019/07/12 10:33
 */

public interface DataTableService {

    public List<Object> getTableList(int id);

    public List<Map<String,Object>> getTableInfo(int id , String tableName ,String type);


}
