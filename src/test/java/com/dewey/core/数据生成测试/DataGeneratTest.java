package com.dewey.core.数据生成测试;

import com.dewey.core.utils.RandomDataUtils;
import com.dewey.core.utils.SQLUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program dataMaker
 * @description: 数据生成测试类
 * @author: xielinzhi
 * @create: 2019/07/24 15:20
 */

public class DataGeneratTest {

    //一些随机数据生成测试
    @Test
    public void testChineseName(){
        for (int i = 0; i <10 ; i++) {
            System.out.println(RandomDataUtils.getChineseName());
            System.out.println(RandomDataUtils.getTel());
            System.out.println(RandomDataUtils.getRndIp());
        }
    }

    //随机生成枚举值测试
    @Test
    public void testEnumData(){
        String colName = "test1";
        String colConfig = "1:1,2:2,3:3,4:4,5:10";
        Map<String,Integer> temp = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String enumData = RandomDataUtils.getEnumData(colName,colConfig);
            if(temp.get(enumData) == null){
                temp.put(enumData,1);
            }else {
                temp.put(enumData,temp.get(enumData)+1);
            }
        }
        for (Integer i = 1; i <=5 ; i++) {
            System.out.println(i+":"+temp.get(i.toString()));
        }
    }

    @Test
    public void getInsert(){
        List<String> colList = new ArrayList<>();
        colList.add("col1");
        colList.add("col2");
        colList.add("col3");
        //System.out.println(SQLUtils.getInsertString("TEST_DATA_TABLE",colList));
    }
}
