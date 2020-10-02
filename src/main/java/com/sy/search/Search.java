package com.sy.search;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Shi Yan
 * @Date 2020/8/7 16:00
 */
public class Search {
    public Driver driver = null;
    public Search(Driver driver) {
        this.driver = driver;
    }

    /**
     * 查找名为name的person信息
     * @param name
     * @return
     */
    public List<String> findPerson(String name) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(p:Person) where p.name = $name return p.name as name,p.id as id",
                            Values.parameters("name", name))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("name").toString() + " : " + record.get("id").toString());
            }
        return result;
    }

    /**
     * 查找名为title的movie信息
     * @param title
     * @return
     */
    public List<String> findMovie(String title) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie) where m.title = $title return m.title as title, m.category as category,m.language as language,m.rate as rate,m.showtime as time,m.length as length",
                            Values.parameters("title", title))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("title").toString() + " : " + record.get("category").toString() + " : " + record.get("language").toString() + " : " + record.get("rate").toString() + " : " + record.get("time").toString() + " : " + record.get("length").toString());
            }
        return result;
    }

    /**
     * 查找名为name的country信息
     * @param name
     * @return
     */
    public List<String> findCountry(String name){
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(c:Country) where c.name = $name return c.name as name,c.id as id",
                            Values.parameters("name", name))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("name").toString() + " : " + record.get("id").toString());
            }
        return result;
    }

    /**
     * 查询名为name person创作的电影
     * @param name
     * @return
     */
    public List<String> movieComposerByPerson(String name) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:composer]->(p:Person{name:$name}) return p.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("name").toString() + " : " + record.get("title").toString());
            }
        return result;
    }

    /**
     * 查询名为name的person导演的电影
     * @param name
     * @return
     */
    public List<String> movieDirectorByPerson(String name) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:director]->(p:Person{name:$name}) return p.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("name").toString() + " : " + record.get("title").toString());
            }
        return result;
    }

    /**
     * 查询名为name的country有哪些电影
     * @param name
     * @return
     */
    public List<String> movieDistrictByCountry(String name) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match(m:Movie)-[:district]->(c:Country{name:$name}) return c.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("name").toString() + " : " + record.get("title").toString());
            }
        return result;
    }

    /**
     * 返回和name一起出演的明星名称和电影名称
     * @param name
     * @return
     */
    public List<String> personActWithOthers(String name) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx ->
                    tx.run("match(p:Person{name:$name})<-[:ACTOR_OF]-(m:Movie)-[:ACTOR_OF]->(other:Person) return m.title as title,other.name as name",
                            Values.parameters("name", name))).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("title").toString() + " : " + record.get("name").toString());
            }
        return result;
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
    public List<String> getMostRatedScoreMovie(String rating, String category) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try (Session session = driver.session()){
            listRecord = session.readTransaction(tx ->
                    tx.run("match (m:Movie) where m.rate>'" + rating + "' and m.category=~'.*" + category+ ".*' return m.title as title order by m.rate desc limit 10")).list();
        }
        if(0 != listRecord.size())
            for(Record record : listRecord){
                result.add(record.get("title").toString());
            }
        return result;
    }

}
