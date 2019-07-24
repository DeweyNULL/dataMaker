package com.dewey.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.dewey.core.service.DataProjectService;
import com.dewey.core.service.DataTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program dataMaker
 * @description: 数据库表解析
 * @author: xielinzhi
 * @create: 2019/07/12 10:00
 */

@RestController
@RequestMapping("/tables")
public class DataTableController {

    @Autowired
    DataTableService dataTableService;

    @PostMapping("/get")
    public ResponseEntity get(@RequestBody Map<String, Object> params){
        int id = (int)params.get("id");
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("body",dataTableService.getTableList(id));
        return ResponseEntity.ok(returnMap);
    }

    @PostMapping("/show")
    public ResponseEntity showTables(@RequestBody Map<String, Object> params){
        int id = (int)params.get("id");
        String tableName = params.get("tableName").toString();
        String type = params.get("type").toString();
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("body",dataTableService.getTableInfo(id,tableName,type));
        return ResponseEntity.ok(returnMap);
    }
}
