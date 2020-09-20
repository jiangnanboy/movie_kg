package com.sy.qa;

import com.hankcs.hanlp.seg.common.Term;
import com.sy.manipulation.basic_operation.GraphSearch;
import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;

import java.util.ArrayList;
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
    public List<String> response(double label, List<Term> listTerm) {
        /**【nm：电影名，nnt：演员名，ng：电影类型】
         * 0:nm 评分
         * 1:nm 上映时间
         * 2:nm 类型
         * 3:nm 简介 (暂时没数据)
         * 4:nm 演员列表
         * 5:nnt 介绍 (暂时没数据)
         * 6:nnt ng 电影作品
         * 7:nnt 电影作品
         * 8:nnt 参演评分 大于 x
         * 9:nnt 参演评分 小于 x
         * 10:nnt 电影类型
         * 11:nnt nnt 合作 电影列表
         * 12:nnt 电影数量
         * 13:nnt 出生日期 (暂时没数据)
         */
        List<String> responseResult = new ArrayList<>();
        List<Record> result = null;
        String movieTitle = null; // 电影名称
        String movieShowtime = null; // 电影上映时间
        String movieCategory = null; // 电影类型
        String movieStar = null; // 演员名称
        String movieOtherStar = null; //另一个演员
        float rate = 0.0f; //评分
        GraphSearch graphSearch = new GraphSearch(driver);
        String questionType = String.valueOf(label);
        switch (questionType) {
            case "0.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.movieRate(movieTitle)) {
                    responseResult.add(record.get("rate").toString());
                }
                break;
            case "1.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.movieShowtime(movieTitle)) {
                    responseResult.add(record.get("showtime").toString());
                }
                break;
            case "2.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.movieCategory(movieTitle)) {
                    responseResult.add(record.get("category").toString());
                }
                break;
            case "3.0": //(暂时没数据)
                break;
            case "4.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.movieActorOfAllPerson(movieTitle)) {
                    responseResult.add(record.get("name").toString());
                }
                break;
            case "5.0": //(暂时没数据)
                break;
            case "6.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                    } else if(StringUtils.equals(term.nature.toString(), "ng")) {
                        movieCategory = term.word;
                    }
                }
                for(Record record : graphSearch.personActorOfCategoryMovie(movieStar, movieCategory)){
                    responseResult.add(record.get("title").toString());
                }
                break;
            case "7.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.movieActorByPerson(movieStar)) {
                    responseResult.add(record.get("title").toString());
                }
                break;
            case "8.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                    } else if(StringUtils.equals(term.nature.toString(), "m")) {
                        rate = Float.parseFloat(term.word);
                    }
                }
                for(Record record : graphSearch.getAboveScorePersonActorOfMovie(movieStar, rate)) {
                    responseResult.add(record.get("title").toString());
                }
                break;
            case "9.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                    } else if(StringUtils.equals(term.nature.toString(), "m")) {
                        rate = Float.parseFloat(term.word);
                    }
                }
                for(Record record : graphSearch.getBelowScorePersonActorOfMovie(movieStar, rate)) {
                    responseResult.add(record.get("title").toString());
                }
                break;
            case "10.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.getCategoryOfMovieActorByPerson(movieStar)) {
                    responseResult.add(record.get("category").toString());
                }
                break;
            case "11.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        if(StringUtils.isBlank(movieStar)) {
                            movieStar = term.word;
                        } else {
                            movieOtherStar = term.word;
                            break;
                        }
                    }
                }
                for(Record record : graphSearch.getMoviesOfPersonActWithOthers(movieStar, movieOtherStar)) {
                    responseResult.add(record.get("title").toString());
                }
                break;
            case "12.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                        break;
                    }
                }
                for(Record record : graphSearch.getCountMovieActorByPerson(movieStar)) {
                    responseResult.add(record.get("count").toString());
                }
                break;
            case "13.0": //(暂时没数据)
                break;
            default:
                System.out.println("抱歉，不懂你説的什么意思？");
                break;
        }
        return responseResult;
    }

}
