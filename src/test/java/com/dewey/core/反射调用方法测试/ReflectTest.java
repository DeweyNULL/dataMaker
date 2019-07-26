package com.dewey.core.反射调用方法测试;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @program dataMaker
 * @description: 反射调用静态方法测试
 * @author: xielinzhi
 * @create: 2019/07/25 14:29
 */

public class ReflectTest {

    @Test
    public void testMethod() throws Exception{
        Class[] paramsType = new Class[]{String.class,String.class};
        String config = "enumData;1:1,2:2,3:3,4:4";
        String[] params = config.split(";");
        Class clazz = Class.forName("com.dewey.core.utils.RandomDataUtils");
        Method method = clazz.getMethod("getEnumData",paramsType);
        String returnString = (String) method.invoke(null,params);
        System.out.println(returnString);
    }
}
