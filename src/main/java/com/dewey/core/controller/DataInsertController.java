package com.dewey.core.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/start")
    public ResponseEntity add(@RequestBody Map<String, Object> params){
        log.info(JSONObject.toJSONString(params));
        Map<String ,Object> returnMap = new HashMap<>();

        return ResponseEntity.ok(returnMap);
    }
}
