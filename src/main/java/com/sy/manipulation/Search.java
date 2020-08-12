package com.sy.manipulation;

import com.sy.base.abs.AbsGraph;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import java.util.List;

/**
 * @Author Shi Yan
 * @Date 2020/8/7 16:00
 */
public class Search extends AbsGraph {

    public Search(Driver driver) {
        super(driver);
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
                    tx.run("match(m:Movie)-[:actor]->(p:Person{name:$name}) return p.name as name,m.title as title",
                            Values.parameters("name", name))).list();
        }
        return listRecord;
    }

    /**
     * 查询名为namer person创作的电影
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

}
