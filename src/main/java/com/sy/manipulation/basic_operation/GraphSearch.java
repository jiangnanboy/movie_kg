package com.sy.manipulation.basic_operation;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import java.util.List;

/**
 * @Author Shi Yan
 * @Date 2020/8/7 16:00
 */
public class GraphSearch {
    public Driver driver = null;
    public GraphSearch(Driver driver) {
        this.driver = driver;
    }

    /**
     * 查询电影评分
     * @param title
     * @return
     */
    public List<Record> movieRate(String title) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("match (m:Movie) where m.title = $title return m.rate",
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
            listRecord = session.readTransaction(tx -> tx.run("match (m:Movie) where m.title = $title return m.showtime",
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
            listRecord = session.readTransaction(tx -> tx.run("match (m:Movie) where m.title = $title return m.category",
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
            listRecord = session.readTransaction(tx -> tx.run("MATCH path=(m:Movie)-[r:ACTOR_OF]->(p:Person) where m.title = $title return p",
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
            listRecord = session.readTransaction(tx -> tx.run("MATCH path=(m:Movie)-[r:ACTOR_OF]->(p:Person) where p.name=$name and m.category =~'.*{category}.*' return m.title",
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
                    tx.run("match (m:Movie)-[:ACTOR_OF]->(p:Person) where m.rate>$rate and p.name=~'.*{person}.*' return m.title",
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
                    tx.run("match (m:Movie)-[:ACTOR_OF]->(p:Person) where m.rate<$rate and p.name=~'.*{person}.*' return m.title",
                            Values.parameters("rate", rating, "person", person))).list();
        }
        return listRecord;
    }

    /**
     * 查找名为name的person信息
     * @param name
     * @return
     */
    public List<Record> findPerson(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(p:Person) where p.name = $name return p.name as name,p.id as id",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 查找名为title的movie信息
     * @param title
     * @return
     */
    public List<Record> findMovie(String title) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie) where m.title = $title return m.title as title, m.category as category,m.language as language,m.rate as rate,m.showtime as time,m.length as length",
                            Values.parameters("title", title))).list();
        }
        return listRecord;
    }

    /**
     * 查找名为name的country信息
     * @param name
     * @return
     */
    public List<Record> findCountry(String name){
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(c:Country) where c.name = $name return c.name as name,c.id as id",
                            Values.parameters("name", name))).list();
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
                    tx.run("match(m:Movie)-[:ACTOR_OF]->(p:Person) where p.name = $name return count(m)",
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
     * 查询名为name person创作的电影
     * @param name
     * @return
     */
    public List<Record> movieComposerByPerson(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:composer]->(p:Person{name:$name}) return p.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 查询名为name的person导演的电影
     * @param name
     * @return
     */
    public List<Record> movieDirectorByPerson(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:director]->(p:Person{name:$name}) return p.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 查询名为name的country有哪些电影
     * @param name
     * @return
     */
    public List<Record> movieDistrictByCountry(String name) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:district]->(c:Country{name:$name}) return c.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 返回和name一起出演的明星名称和电影名称
     * @param name
     * @return
     */
    public List<Record> personActWithOthers(String name) {
        List<Record> lisRecord = null;
        try(Session session = driver.session()) {
            lisRecord = session.readTransaction(tx ->
                    tx.run("match(p:Person{name:$name})<-[:ACTOR_OF]-(m:Movie)-[:ACTOR_OF]->(other:Person) return m.title as title,other.name as name",
                            Values.parameters("name", name))).list();
        }
        return lisRecord;
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

    /**
     * name1与name2最短路径
     * @param name1
     * @param name2
     * @return
     */
    public List<Record> shortestPathBetweenPerson(String name1, String name2) {
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx ->
                    tx.run("match (m:Person{name:$mname}),(n:Person{name:$nname}),p=shortestpath((m)-[*..]-(n)) return p",
                    Values.parameters("mname", name1, "nname", name2))).list();
        }
        return listRecord;
    }

    /**
     * 返回类型为category，评分最高的前10部电影名称
     * @param rating
     * @param category
     * @return
     */
    public List<Record> getMostRatedScoreMovie(float rating, String category) {
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match (m:Movie) where m.rate>" + rating+ " and m.category=~'.*" + category+ ".*' return m.title order by m.rate desc limit 10")).list();
        }
        return listRecord;
    }

}
