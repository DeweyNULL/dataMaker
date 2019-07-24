package com.dewey.core.controller;

import com.dewey.core.pojo.DataProject;
import com.dewey.core.service.DataProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @program dataMaker
 * @description: controller
 * @author: xielinzhi
 * @create: 2019/07/11 17:50
 */

@RestController
@RequestMapping("/project")
@Slf4j
public class DataProjectController {

    @Autowired
    DataProjectService dataProjectService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody DataProject dataProject){
        log.info(JSONObject.toJSONString(dataProject));
        Map<String ,Object> returnMap = new HashMap<>();
        if(dataProjectService.saveOrUpdate(dataProject)){
            returnMap.put("msg","succeed");
        }else {
            returnMap.put("msg","failed");
        }

        return ResponseEntity.ok(returnMap);
    }

    @GetMapping("/show")
    public ResponseEntity get(){
        log.info("进入库内容展示");
        Map<String ,Object> returnMap = new HashMap<>();
        if(dataProjectService.count()>0){

        }
        returnMap.put("msg","ok");
        return ResponseEntity.ok(returnMap);
    }
}
