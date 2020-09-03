package com.sy.mainclass;

import com.sy.manipulation.basic_operation.Search;
import com.sy.manipulation.cypher.CreateNode;
import com.sy.manipulation.cypher.CreateRelation;
import com.sy.util.InitNeo4j;

import com.sy.util.PropertiesReader;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;

import java.util.List;

/**
 * Created by YanShi on 2020/7/24 1:11 下午
 */
public class GraphCypherBuild {
    static Driver driver = null;
    public static void main(String[] args) {
        createNode(driver);
        createRelation(driver);
        //search(driver);
        InitNeo4j.closeDriver();

    }

    static {
        String url = PropertiesReader.get("url");
        String name = PropertiesReader.get("name");
        String password = PropertiesReader.get("password");
        driver = InitNeo4j.initDriver(url, name, password);
    }

    /**
     * 创建entity
     * @param driver
     */
    public static void createNode(Driver driver) {
        String personEntityCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("personEntity")).getPath().substring(1);
        String movieEntityCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("movieEntity")).getPath().substring(1);
        String countryEntityCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("countryEntity")).getPath().substring(1);
        String[] entities = {personEntityCsv, movieEntityCsv, countryEntityCsv};
        CreateNode createNode = new CreateNode(driver);
        createNode.createPersonNode(entities[0]);
        createNode.createMovieNode(entities[1]);
        createNode.createCountryNode(entities[2]);
    }

    /**
     * 创建relationship
     * @param driver
     */
    public static void createRelation(Driver driver) {
        String actorRelationCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("actorRelation")).getPath().substring(1);
        String composerRelationCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("composerRelation")).getPath().substring(1);
        String directorRelationCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("directorRelation")).getPath().substring(1);
        String districtRelationCsv = GraphCypherBuild.class.getClassLoader().getResource(PropertiesReader.get("districtRelation")).getPath().substring(1);
        String[] relations = {actorRelationCsv, composerRelationCsv, directorRelationCsv, districtRelationCsv};
        CreateRelation createRelation = new CreateRelation(driver);
        createRelation.createActorRel(relations[0]);
        createRelation.createComposerRel(relations[1]);
        createRelation.createDirectorRel(relations[2]);
        createRelation.createDistrictRel(relations[3]);
    }

    /**
     *  基本查询
     * @param driver
     */
    public static void search(Driver driver) {
        Search search = new Search(driver);
        List<Record> listRecord = null;
        listRecord = search.getMostRatedScoreMovie((float) 7.5, "科幻");
        printListRecord(listRecord);
    }

    /**
     * 打印结果
     * @param listRecord
     */
    public static void printListRecord(List<Record> listRecord) {
        listRecord.forEach(list->System.out.println(list));
    }


}
