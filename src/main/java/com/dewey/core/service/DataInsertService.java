package com.dewey.core.service;

public interface DataInsertService {
    /**
     * 插数据任务入口
     * @param id
     * @param size
     */
    public String insertTask(int id , int size, String tableName);

    /**
     * 查看插入数据的进度的函数
     * @param id
     * @param tableName
     * @param size
     * @return
     */
    public long checkTask(int id , String tableName , int size);

}
