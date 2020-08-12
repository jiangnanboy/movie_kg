package com.sy.node;

import com.sy.base.abs.Node;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by YanShi on 2020/7/30 4:00 下午
 */
public class Person extends Node{
    /**
     * "name","id:ID",":LABEL"
     */

    @CsvBindByName(column = "name")
    public String name;

}
