package com.sy.util;

import org.neo4j.driver.v1.*;

/**
 * Created by YanShi on 2020/7/24 9:22 上午
 */
public class InitNeo4j {
    private static Driver driver = null;
    static String url = null;
    static String name = null;
    static String password = null;
    /**
     * 初始化 driver
     * @param url
     * @param name
     * @param password
     * @return
     */
    private static Driver initDriver(String url, String name, String password) {
        if(null == driver) {
            driver = GraphDatabase.driver(url, AuthTokens.basic(name, password));
        }
        return driver;
    }

    /**
     * 关闭 driver
     */
    public static void closeDriver() {
        if(null != driver) {
            driver.close();
        }
    }

    public static Driver getDriver() {
        return driver;
    }

    static {
        url = PropertiesReader.get("url");
        name = PropertiesReader.get("name");
        password = PropertiesReader.get("password");
        driver = InitNeo4j.initDriver(url, name, password);
    }

}
