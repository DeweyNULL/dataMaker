package com.dewey.core.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Tool utils.
 *
 */
public class ToolUtils {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ToolUtils.class);

    /**
     * The constant DF.
     */
    private static final DecimalFormat DF = new DecimalFormat("#0.00");

    /**
     * The constant DB2.
     */
    private static final String DB2 = "db2";
    /**
     * The constant MYSQL.
     */
    private static final String MYSQL = "mysql";
    /**
     * The constant ORACLE.
     */
    private static final String ORACLE = "oracle";

    /**
     * The constant MYSQL_DRIVER_CLASS_NAME.
     */
    private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    /**
     * The constant ORACLE_DRIVER_CLASS_NAME.
     */
    private static final String ORACLE_DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
    /**
     * The constant DB2_DRIVER_CLASS_NAME.
     */
    private static final String DB2_DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";

    /**
     * The constant LIMIT.
     */
    private static final int LIMIT = 1000;

    static{
        /*四舍五入*/
        DF.setRoundingMode(RoundingMode.HALF_UP);
    }


    public static String formatAmt(Object rawVal){
        return formatAmt(rawVal, false);
    }

    /**
     * 金额格式化.
     *
     */
    public static String formatAmt(Object rawVal, boolean unit){
        String format = DF.format(rawVal);

        if(unit){
            format += "元";
        }

        return format;
    }

    /**
     * 小数处理.
     *
     */
    public static String formatDecimal(Object rawVal, int digit){
        String pattern = "#0";
        if(digit > 0){
            pattern += ".";
            for(int i=0;i<digit;i++){
                pattern += "0";
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        return decimalFormat.format(rawVal);
    }

    /**
     * 对数字的空位进行补零操作；例如需要三位字符串但当前数字是1，补零后转换成字符串“001”.
     *
     */
    public static String addZeros(Object num, Integer digit){
        String r = "";

        String numStr = String.valueOf(num);
        int numLength = numStr.length();

        if(numLength < digit){
            for(int i=0;i<(digit - numLength);i++){
                r += "0";
            }
        }
        r += numStr;

        return r;
    }

    /**
     * 根据url获取数据库类型
     *
     */
    public static String dbType(String url){
        if(url == null || "".equals(url)) return "";
        String r = "";

        if(url.contains(MYSQL)){
            r = MYSQL;
        }
        else if(url.contains(ORACLE)){
            r = ORACLE;
        }
        else if(url.contains(DB2)){
            r = DB2;
        }

        return r;
    }

    /**
     * Driver class name string.
     *
     */
    public static String driverClassName(String url){
        if(url == null || "".equals(url)) return "";
        String r = "";

        if(url.contains(MYSQL)){
            r = MYSQL_DRIVER_CLASS_NAME;
        }
        else if(url.contains(ORACLE)){
            r = ORACLE_DRIVER_CLASS_NAME;
        }
        else if(url.contains(DB2)){
            r = DB2_DRIVER_CLASS_NAME;
        }

        return r;
    }

    /**
     * Connection test sql string.
     *
     */
    public static String connectionTestSql(String url){
        String r = "";

        if(url.contains(MYSQL)){
            r = "SELECT 1 FROM DUAL";
        }
        else if(url.contains(ORACLE)){
            r = "SELECT 1 FROM DUAL";
        }
        else if(url.contains(DB2)){
            r = "SELECT 1 FROM SYSIBM.SYSDUMMY1";
        }

        return r;
    }

    /**
     * List to map .
     */
    public static <S, T> Map<S, T> list2Map(List<T> dataList, String keyParam){
        Map<S, T> r = Maps.newHashMapWithExpectedSize(dataList.size());

        for(T t : dataList){
            Class clazz = t.getClass();
            try {
                Method method = clazz.getMethod("get" + StringUtils.capitalize(keyParam));

                S keyValue = (S) method.invoke(t);
                if(null != keyValue){
                    r.put(keyValue, t);
                }

            } catch (Exception e) {
                LOGGER.error("反射设值出错", e);
                break;
            }
        }

        return r;
    }

    /**
     * Reverse hash map.
     *
     */
    public static <K,V> HashMap<V,K> swap(Map<K,V> map) {
        HashMap<V,K> swap = new HashMap<V, K>();
        map.entrySet().stream().forEach((entry)->swap.put(entry.getValue(),entry.getKey()));
        return swap;
    }

    public static String schemaName(String... params) {
        String r = "";
        String url = params[0];
        if (url.indexOf("mysql") > -1) {
            if (url.indexOf("?") > -1) {
                url = url.replace("?", "~");
                url = url.split("~")[0];
            }

            r = url.split("/")[url.split("/").length - 1].trim();
        } else if (url.indexOf("oracle") > -1) {
            r = params[1].trim();
        }

        return r;
    }
}
