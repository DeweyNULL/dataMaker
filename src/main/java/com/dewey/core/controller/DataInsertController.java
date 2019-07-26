package com.dewey.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.dewey.core.service.DataInsertService;
import com.dewey.core.utils.MapUtils;
import com.dewey.core.utils.TaskMapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program dataMaker
 * @description: 数据开始插入
 * @author: xielinzhi
 * @create: 2019/07/24 20:11
 */

@RestController
@RequestMapping("/insert")
@Slf4j
public class DataInsertController {

    @Autowired
    DataInsertService dataInsertService;

    @PostMapping("/start")
    public ResponseEntity add(@RequestBody Map<String, Object> params){
        Map<String ,Object> returnMap = new HashMap<>();
        int id = MapUtils.getInteger(params,"id",0);
        int size = MapUtils.getInteger(params,"size",0);
        String tableName = MapUtils.getString(params,"tableName","");
        returnMap.put("msg",dataInsertService.insertTask(id,size,tableName));
        return ResponseEntity.ok(returnMap);
    }

    @PostMapping("/check")
    public ResponseEntity getSchedule(@RequestBody Map<String, Object> params){
        Map<String ,Object> returnMap = new HashMap<>();
        int id = MapUtils.getInteger(params,"id",0);
        int size = MapUtils.getInteger(params,"size",0);
        String tableName = MapUtils.getString(params,"tableName","");
        returnMap.put("schedule",dataInsertService.checkTask(id,tableName,size));
        return ResponseEntity.ok(returnMap);
    }
}
