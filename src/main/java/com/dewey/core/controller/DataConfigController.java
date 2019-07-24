package com.dewey.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.dewey.core.pojo.DataConfig;
import com.dewey.core.pojo.DataProject;
import com.dewey.core.service.DataConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program dataMaker
 * @description: 数据配置项的controller
 * @author: xielinzhi
 * @create: 2019/07/24 15:34
 */

@RestController
@RequestMapping("/config")
@Slf4j
public class DataConfigController {

    @Autowired
    DataConfigService dataConfigService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody List<DataConfig> dataConfig){
        log.info(JSONObject.toJSONString(dataConfig));

        Map<String ,Object> returnMap = new HashMap<>();
        try {
            dataConfigService.saveBatch(dataConfig);
            returnMap.put("msg","ok!");
        }catch (Exception e){
            returnMap.put("msg","数据批量插入数据配置项出错！");
            log.error("数据批量插入数据配置项出错",e);
        }
        return ResponseEntity.ok(returnMap);
    }
}
