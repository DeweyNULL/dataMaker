package com.dewey.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dewey.core.mapper.DataConfigMapper;
import com.dewey.core.mapper.DataProjectMapper;
import com.dewey.core.pojo.DataConfig;
import com.dewey.core.pojo.DataProject;
import com.dewey.core.service.DataInsertService;
import com.dewey.core.utils.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @program dataMaker
 * @description: 插入数据的任务
 * @author: xielinzhi
 * @create: 2019/07/25 14:56
 */
@Slf4j
@Service
public class DataInsertServiceImpl implements DataInsertService {

    @Autowired
    DataConfigMapper dataConfigMapper;

    @Autowired
    DataProjectMapper dataProjectMapper;

    @Value("${insertServiceThreadSize:10}")
    int threadSize;

    @Value("${batchSize:100}")
    int batchSize;

    @Autowired
    @Qualifier("insertThreadPool")
    ThreadPoolExecutor insertThreadPool;

    @Override
    public String insertTask(int id, int size ,String tableName) {
        String key = id+"_"+tableName;
        if(TaskMapUtils.hasItem(key)){
            return "已经存在该表的插入任务，请等待任务结束";
        }else {
            checkTask( id ,tableName ,size);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    QueryWrapper<DataConfig> queryWrapper =new QueryWrapper<>();
                    queryWrapper.eq("PROJECT_ID",id).eq("PROJECT_TABLE_NAME",tableName);
                    List<DataConfig> dataConfigList = dataConfigMapper.selectList(queryWrapper);
                    if(ListUtils.isNotEmpty(dataConfigList)) {
                        //测试写法 后删除
                        DataProject dataProject = dataProjectMapper.selectById(id);
                        //获取插入语句
                        List<String> colNameList = new ArrayList<>();
                        List<String> colTypeList = new ArrayList<>();
                        List<Method> methods = new ArrayList<>();
                        List<String[]> paramList = new ArrayList<>();
                        try {
                            Class clazz = Class.forName(RandomDataUtils.className);
                            for (DataConfig dataconfig : dataConfigList) {
                                colNameList.add(dataconfig.getCloName());
                                colTypeList.add(dataconfig.getCloType());
                                String[] config = dataconfig.getCloConfig() == null ? null : dataconfig.getCloConfig().split(";");
                                if (config != null && config.length > 0) {
                                    Class[] paramType = new Class[config.length - 1];
                                    String[] param = new String[config.length - 1];
                                    for (int i = 0; i < paramType.length; i++) {
                                        paramType[i] = String.class;
                                        param[i] = config[i + 1];
                                    }
                                    Method method = clazz.getMethod(config[0], paramType);
                                    methods.add(method);
                                    paramList.add(param);
                                }
                            }
                        } catch (Exception e) {
                            log.error("获取类失败", e);
                        }
                        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.get(id, dataProject.getDatasource(), dataProject.getUsername(), dataProject.getPassword());
                        try {
                            long startTime = System.currentTimeMillis();
                            log.info("开始对表'{}'插入数据，数据量为'{}'", tableName, size);
                            ThreadFactory threadFactory = new ThreadFactoryBuilder()
                                    .setNameFormat("insert-task-thread")
                                    .build();
                            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadSize, threadSize, 0L,
                                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),threadFactory);
                            List<Task> tasks = new ArrayList<>();
                            for (int tasksSize = size; tasksSize > 0; tasksSize -= batchSize) {
                                int insertSize = tasksSize - batchSize > 0 ? batchSize : tasksSize;
                                tasks.add(new Task(jdbcTemplate, dataProject.getType(), dataConfigList.get(0).getProjectTableName(),
                                        colNameList, colTypeList, methods, paramList, insertSize));
                            }
                            List<Future<Integer>> futures = threadPoolExecutor.invokeAll(tasks);
                            for (Future f : futures) {
                                f.get();
                            }
                            long endTime = System.currentTimeMillis();
                            log.info("对表'{}'插入数据已经完成。总计耗时:{}ms", tableName, endTime - startTime);
                            threadPoolExecutor.shutdown();
                        } catch (Exception e) {
                            log.error("多线程插入数据失败", e);
                        }
                    }
                    String key = id + "_" + tableName;
                    TaskMapUtils.removeId(key);
                }
            };
            insertThreadPool.execute(runnable);
        }
        return "开始执行插入任务";
    }

    @Override
    public long checkTask(int id , String tableName, int size){
        DataProject dataProject = dataProjectMapper.selectById(id);
        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.get(id, dataProject.getDatasource(), dataProject.getUsername(), dataProject.getPassword());
        String key = id + "_" + tableName;
        int last = TaskMapUtils.getIntegerItem(key);
        try {
            int count = jdbcTemplate.queryForObject(SQLUtils.getTableCountSql(tableName),Integer.class);
            if(!TaskMapUtils.hasItem(key) && last == 0){
                //last为0 说明这个任务可能是不存在taskMap中 或者 数据库没有数据
                if(count>0){
                    TaskMapUtils.putItem(key,count);
                }else {
                    TaskMapUtils.putItem(key,last);
                }
            }
            //获取任务开始时候的数据量
            int begin = TaskMapUtils.getIntegerItem(key);
            if(size!=0){
                return Math.round((double)(count-begin)/size*100);
            }
        }catch (Exception e){
            log.error("查询进度出错",e);
        }
        return last;
    }

    class Task implements Callable<Integer>{

        private JdbcTemplate jdbcTemplate ;
        private String dbtype;
        private String tableName;
        private List<String> colNameList;
        private List<String> colTypeList;
        private List<Method> methods;
        private List<String[]> paramList;
        int times;

        public Task(JdbcTemplate jdbcTemplate, String dbtype, String tableName,
                    List<String> colNameList, List<String> colTypeList,
                    List<Method> methods, List<String[]> paramList, int times) {
            this.jdbcTemplate = jdbcTemplate;
            this.dbtype = dbtype;
            this.tableName = tableName;
            this.colNameList = colNameList;
            this.colTypeList = colTypeList;
            this.methods = methods;
            this.paramList = paramList;
            this.times = times;
        }

        @Override
        public Integer call() throws Exception {
            List<String> colValueList = new ArrayList<>();
            String[] sqls = new String[times];
            for (int i = 0; i < times; i++) {
                for (int j = 0; j < methods.size(); j++) {
                    colValueList.add((String) methods.get(j).invoke(null,paramList.get(j)));
                }
                String insertSql = SQLUtils.getInsertString(tableName,dbtype, colNameList,colTypeList,colValueList);
                sqls[i] = insertSql;
                colValueList.clear();
            }
            jdbcTemplate.batchUpdate(sqls);
            return 1;
        }
    }
}
