package com.dewey.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program dataMaker
 * @description: 同一时间，对于同一个表只能有一个任务
 * @author: xielinzhi
 * @create: 2019/07/26 13:55
 */

public class TaskMapUtils {

    private TaskMapUtils(){};

    public static Map<String,Object> taskMap = new HashMap<>();

    public static void putItem(String id , Object item){

        if(taskMap.get(id) == null){
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                if(taskMap.get(id) == null){
                    taskMap.put(id,item);
                }
            }finally {
                lock.unlock();
            }
        }
    }

    public static boolean hasItem(String id){
        return taskMap.get(id) != null ;
    }

    public static void removeId(String id){
        if(taskMap.get(id) != null){
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                if(taskMap.get(id) != null){
                    taskMap.remove(id);
                }
            }finally {
                lock.unlock();
            }
        }
    }

    public static Integer getIntegerItem(String id){
        return MapUtils.getInteger(taskMap,id,0);
    }
}
