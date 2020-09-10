package com.sy.util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

/**
 * Created by YanShi on 2020/9/9 21:22
 */
public class InitSparkSession {

    private static SparkSession sparkSession = null;
    private static JavaSparkContext javaSparkContext = null;
    private static SparkConf sparkConf = null;

    private static void initSparkConf() {
        sparkConf = new SparkConf()
                .setAppName("ProcessData")
                .setMaster("local[*]")
                .set("spark.executor.memory", "4g")
                .set("spark.network.timeout", "1000")
                .set("spark.sql.broadcastTimeout", "2000")
                .set("spark.executor.heartbeatInterval", "100");
    }

    private static void initSparkSession() {
        if(null == sparkConf)
            initSparkConf();
        sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        /*sparkSession = SparkSession
                .builder()
                .appName("Test")
                .master("local[*")
                .config("spark.executor.memory", "4g")
                .getOrCreate();*/
    }

    private static void initJavaSparkContext() {
        if(null == sparkConf)
            initSparkConf();
        javaSparkContext = new JavaSparkContext(sparkConf);
    }

    public static SparkSession getSparkSession() {
        if(null == sparkSession)
            initSparkSession();
        return sparkSession;
    }

    public static JavaSparkContext getJavaSparkContext() {
        if(null == javaSparkContext)
            initJavaSparkContext();
        return javaSparkContext;
    }

    public static void closeSparkSession() {
        if(null != sparkSession)
            sparkSession.close();
    }

    public static void closeJavaSparkContext() {
        if(null != javaSparkContext)
            javaSparkContext.close();
    }

    public static void closeSparkSessionAndJavaContext() {
        if(null != sparkSession)
            sparkSession.close();
        if(null != javaSparkContext)
            javaSparkContext.close();
    }

}
