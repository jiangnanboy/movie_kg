package com.sy.qa;

import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

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
        String questionType = String.valueOf(label);
        switch (questionType) {
            case "0.0":
                for(Term term : listTerm) {
                    if(StringUtils.equals(term.nature.toString(), "nm")) {
                        movieTitle = term.word;
                        break;
                    }
                }
                for(Record record : movieRate(movieTitle)) {
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
                for(Record record : movieShowtime(movieTitle)) {
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
                for(Record record : movieCategory(movieTitle)) {
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
                for(Record record : movieActorOfAllPerson(movieTitle)) {
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
                for(Record record : personActorOfCategoryMovie(movieStar, movieCategory)){
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
                for(Record record : movieActorByPerson(movieStar)) {
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
                for(Record record : getAboveScorePersonActorOfMovie(movieStar, rate)) {
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
                for(Record record : getBelowScorePersonActorOfMovie(movieStar, rate)) {
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
                for(Record record : getCategoryOfMovieActorByPerson(movieStar)) {
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
                for(Record record : getMoviesOfPersonActWithOthers(movieStar, movieOtherStar)) {
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
                for(Record record : getCountMovieActorByPerson(movieStar)) {
                    responseResult.add(record.get("count").toString());
                }
                break;
            case "13.0": //(暂时没数据)
                break;
            default:
                System.out.println("真的不好意思，我还小呢，不懂您在説些什么？");
                break;
        }
        return responseResult;
    }

    /**
     * 查询电影评分
     * @param title
     * @return
     */
    public List<Record> movieRate(String title) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("match (m:Movie) where m.title = $title return m.rate as rate",
                    Values.parameters("title", title))).list();
        }
        return listRecord;
    }

    /**
     * 查询电影上映时间
     * @param title
     * @return
     */
    public List<Record> movieShowtime(String title) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("match (m:Movie) where m.title = $title return m.showtime as showtime",
                    Values.parameters("title", title))).list();
        }
        return listRecord;
    }

    /**
     * 查询电影类型
     * @param title
     * @return
     */
    public List<Record> movieCategory(String title) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("match (m:Movie) where m.title = $title return m.category as category",
                    Values.parameters("title", title))).list();
        }
        return listRecord;
    }

    /**
     * 查询参演该电影的演员
     * @param title
     * @return
     */
    public List<Record> movieActorOfAllPerson(String title) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("MATCH path=(m:Movie)-[r:ACTOR_OF]->(p:Person) where m.title = $title return p.name as name",
                    Values.parameters("title", title))).list();
        }
        return listRecord;
    }

    /**
     * 演员参的类型为category的电影
     * @param person
     * @param category
     * @return
     */
    public List<Record> personActorOfCategoryMovie(String person, String category) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("MATCH path=(m:Movie)-[r:ACTOR_OF]->(p:Person) where p.name=$name and m.category =~'.*{category}.*' return m.title as title",
                    Values.parameters("name", person, "category", category))).list();
        }
        return listRecord;
    }

    /**
     * 返回演员为person参演的评分高于rating的电影影名称
     * @param person
     * @param rating
     * @return
     */
    public List<Record> getAboveScorePersonActorOfMovie(String person, float rating) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match (m:Movie)-[:ACTOR_OF]->(p:Person) where m.rate>$rate and p.name=~'.*{person}.*' return m.title as title",
                            Values.parameters("rate", rating, "person", person))).list();
        }
        return listRecord;
    }

    /**
     * 返回演员为person参演的评分低于rating的电影影名称
     * @param person
     * @param rating
     * @return
     */
    public List<Record> getBelowScorePersonActorOfMovie(String person, float rating) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match (m:Movie)-[:ACTOR_OF]->(p:Person) where m.rate<$rate and p.name=~'.*{person}.*' return m.title as title",
                            Values.parameters("rate", rating, "person", person))).list();
        }
        return listRecord;
    }

    /**
     * 查询名为name的person参演的电影
     * @param name
     * @return
     */
    public List<Record> movieActorByPerson(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:ACTOR_OF]->(p:Person{name:$name}) return m.title as title",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 查询名为name的person参演的电影数量
     * @param name
     * @return
     */
    public List<Record> getCountMovieActorByPerson(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:ACTOR_OF]->(p:Person) where p.name = $name return count(m) as count",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 查询名为name的person参演的电影类型(风格)
     * @param name
     * @return
     */
    public List<Record> getCategoryOfMovieActorByPerson(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:ACTOR_OF]->(p:Person{name:$name}) return m.category as category",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * name1和name2共同出演的电影
     * @param name1
     * @param name2
     * @return
     */
    public List<Record> getMoviesOfPersonActWithOthers(String name1, String name2) {
        List<Record> lisRecord = null;
        try(Session session = driver.session()) {
            lisRecord = session.readTransaction(tx ->
                    tx.run("match(p:Person{name:$pname})<-[:ACTOR_OF]-(m:Movie)-[:ACTOR_OF]->(other:Person{name:$oname}) return m.title as title",
                            Values.parameters("pname", name1, "oname", name2))).list();
        }
        return lisRecord;
    }

}
