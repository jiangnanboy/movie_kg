package com.sy.mainclass;

import com.sy.manipulation.basic_operation.GraphSearch;
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
    public static void main(String[] args) {
        createNode(InitNeo4j.getDriver());
        createRelation(InitNeo4j.getDriver());
        InitNeo4j.closeDriver();

    }

    /**
     * 创建entity
     * @param driver
     */
    public static void createNode(Driver driver) {
        String personEntityCsv = PropertiesReader.get("personEntity");
        String movieEntityCsv = PropertiesReader.get("movieEntity");
        String countryEntityCsv = PropertiesReader.get("countryEntity");
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
        String actorRelationCsv = PropertiesReader.get("actorRelation");
        String composerRelationCsv = PropertiesReader.get("composerRelation");
        String directorRelationCsv = PropertiesReader.get("directorRelation");
        String districtRelationCsv = PropertiesReader.get("districtRelation");
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
        GraphSearch search = new GraphSearch(driver);
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
