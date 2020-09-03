package com.sy.manipulation.cypher.node;

import com.sy.base.abs.AbstractNode;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by YanShi on 2020/7/30 4:00 下午
 */
public class Person extends AbstractNode {
    /**
     * "name","id:ID",":LABEL"
     */

    @CsvBindByName(column = "name")
    public String name;

}
