package com.sy.base.abs;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by YanShi on 2020/7/30 5:25 下午
 */
public abstract class Node{

    @CsvBindByName(column = "id:ID")
    public String id;

    @CsvBindByName(column = ":LABEL")
    public String label;
}
