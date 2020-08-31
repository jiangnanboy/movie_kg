package com.sy.base.abs;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by YanShi on 2020/7/31 10:53 上午
 */
public abstract class AbstractRelation {

    @CsvBindByName(column = ":START_ID")
    public String startID;

    @CsvBindByName(column = ":END_ID")
    public String endID;
}
