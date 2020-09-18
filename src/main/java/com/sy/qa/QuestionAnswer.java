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
        List<Record> result = null;
        String movieTitle = null; // 电影名称
        String movieShowtime = null; // 电影上映时间
        String movieCategory = null; // 电影类型
        String movieStar = null; // 演员名称
        String movieOtherStar = null; //另一个演员
        float rate = 0.0f; //评分
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
            case 3: //(暂时没数据)
                break;
            case 4:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                result = graphSearch.movieActorOfAllPerson(movieTitle);
                break;
            case 5: //(暂时没数据)
                break;
            case 6:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                    } else if(StringUtils.equals(term.nature.toString(), "ng")) {
                        movieCategory = term.word;
                    }
                }
                result = graphSearch.personActorOfCategoryMovie(movieStar, movieCategory);
                break;
            case 7:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                        break;
                    }
                }
                result = graphSearch.movieActorByPerson(movieStar);
                break;
            case 8:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                    } else if(StringUtils.equals(term.nature.toString(), "m")) {
                        rate = Float.parseFloat(term.word);
                    }
                }
                result = graphSearch.getAboveScorePersonActorOfMovie(movieStar, rate);
                break;
            case 9:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                    } else if(StringUtils.equals(term.nature.toString(), "m")) {
                        rate = Float.parseFloat(term.word);
                    }
                }
                result = graphSearch.getBelowScorePersonActorOfMovie(movieStar, rate);
                break;
            case 10:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                        break;
                    }
                }
                result = graphSearch.getCategoryOfMovieActorByPerson(movieStar);
                break;
            case 11:
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
                result = graphSearch.getMoviesOfPersonActWithOthers(movieStar, movieOtherStar);
                break;
            case 12:
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nnt")) {
                        movieStar = term.word;
                        break;
                    }
                }
                result = graphSearch.getCountMovieActorByPerson(movieStar);
                break;
            case 13: //(暂时没数据)
                break;
            default:
                System.out.println("抱歉，不懂你説的什么意思？");
                break;
        }
        return result;
    }

}
