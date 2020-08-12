package com.sy.node;

import com.opencsv.bean.CsvBindByName;
import com.sy.base.abs.Node;

/**
 * Created by YanShi on 2020/7/30 4:00 下午
 */
public class Movie extends Node {
    /**"title",
     * "url",
     * "cover",
     * "rate",
     * "category:String[]",
     * "language:String[]",
     * "showtime",
     * "length",
     * "othername:String[]",
     * "id:ID",
     * ":LABEL"
     */

    @CsvBindByName(column = "title")
    public String title;

    @CsvBindByName(column = "url")
    public String url;

    @CsvBindByName(column = "cover")
    public String cover;

    @CsvBindByName(column = "rate")
    public float rate;

    @CsvBindByName(column = "category:String[]")
    public String category;

    @CsvBindByName(column = "language:String[]")
    public String language;

    @CsvBindByName(column = "showtime")
    public float showtime;

    @CsvBindByName(column = "length")
    public float length;

    @CsvBindByName(column = "othername:String[]")
    public String othername;

}















