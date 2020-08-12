package com.sy.node;

import com.opencsv.bean.CsvBindByName;
import com.sy.base.abs.Node;

/**
 * Created by YanShi on 2020/7/30 4:01 下午
 */
public class Country extends Node {
    /**
     * "name","id:ID",":LABEL"
     */

    @CsvBindByName(column = "name")
    public String name;
}
