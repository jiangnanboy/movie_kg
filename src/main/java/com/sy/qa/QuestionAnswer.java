package com.sy.qa;

import com.hankcs.hanlp.seg.common.Term;
import com.sy.manipulation.basic_operation.GraphSearch;
import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;

import java.util.List;

/**
 * @author YanShi
 * @date 2020/8/28 20:29
 */
public class QuestionAnswer {
    Driver driver = null;
    public QuestionAnswer(Driver driver) {
        this.driver = driver;
    }

    /**
     * 回答
     * @param label
     * @param listTerm
     */
    public List<Record> response(int label, List<Term> listTerm) {
        /**
         * 0:nm 评分
         * 1:nm 上映时间
         * 2:nm 类型
         * 3:nm 简介
         * 4:nm 演员列表
         * 5:nnt 介绍
         * 6:nnt ng 电影作品
         * 7:nnt 电影作品
         * 8:nnt 参演评分 大于 x
         * 9:nnt 参演评分 小于 x
         * 10:nnt 电影类型
         * 11:nnt nnt 合作 电影列表
         * 12:nnt 电影数量
         * 13:nnt 出生日期
         */
        List<Record> result = null;
        String movieTitle = null;
        String movieShowtime = null;
        String movieCategory = null;
        GraphSearch graphSearch = new GraphSearch(driver);
        switch (label) {
            case 0:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                result = graphSearch.movieRate(movieTitle);
                break;
            case 1:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                result = graphSearch.movieShowtime(movieTitle);
                break;
            case 2:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                result = graphSearch.movieCategory(movieTitle);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            default:
                System.out.println("抱歉，不懂你説的什么意思？");
                break;
        }
        return result;
    }

}
