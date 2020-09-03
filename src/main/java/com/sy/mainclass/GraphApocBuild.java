package com.sy.mainclass;


import com.sy.manipulation.apoc.CreateNode;
import com.sy.manipulation.apoc.CreateRelation;
import com.sy.type.LabelTypes;
import com.sy.type.RelTypes;
import com.sy.util.InitNeo4j;
import com.sy.util.PropertiesReader;
import org.neo4j.driver.v1.Driver;

/**
 * Created by YanShi on 2020/7/24 1:10 下午
 */
public class GraphApocBuild {

    static Driver driver = null;
    public static void main(String[] args) {
        createNode(driver);
        createRelation(driver);
        InitNeo4j.closeDriver();
    }

    static {
        String url = PropertiesReader.get("url");
        String name = PropertiesReader.get("name");
        String password = PropertiesReader.get("password");
        driver = InitNeo4j.initDriver(url, name, password);
    }

    /**
     * 构建 node
     * @param driver
     */
    public static void createNode(Driver driver) {
        CreateNode createNode = new CreateNode(driver);
        createNode.createNode("/node/Person.csv", LabelTypes.PERSON.name());
        createNode.createOnlyIndex(LabelTypes.PERSON.name(), "id");
        createNode.createNode( "/node/Movie.csv", LabelTypes.MOVIE.name());
        createNode.createOnlyIndex(LabelTypes.MOVIE.name(), "id");
        createNode.createNode( "/node/Country.csv", LabelTypes.COUNTRY.name());
        createNode.createOnlyIndex(LabelTypes.COUNTRY.name(), "id");
    }

    /**
     * 构建 relation
     * @param driver
     */
    public static void createRelation(Driver driver) {
        CreateRelation createRelation = new CreateRelation(driver);
        createRelation.createRelation("/relation/actor.csv", LabelTypes.MOVIE.name(), LabelTypes.PERSON.name(), RelTypes.ACTOR.name());
        createRelation.createRelation("/relation/composer.csv", LabelTypes.MOVIE.name(), LabelTypes.PERSON.name(), RelTypes.COMPOSER.name());
        createRelation.createRelation("/relation/director.csv", LabelTypes.MOVIE.name(), LabelTypes.PERSON.name(), RelTypes.DIRECTOR.name());
        createRelation.createRelation("/relation/district.csv", LabelTypes.MOVIE.name(), LabelTypes.COUNTRY.name(), RelTypes.DISTRICT.name());
    }


}
