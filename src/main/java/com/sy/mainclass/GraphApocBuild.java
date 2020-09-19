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
    public static void main(String[] args) {
        createNode(InitNeo4j.getDriver());
        createRelation(InitNeo4j.getDriver());
        InitNeo4j.closeDriver();
    }

    /**
     * 构建 node
     * @param driver
     */
    public static void createNode(Driver driver) {
        CreateNode createNode = new CreateNode(driver);

        createNode.createNode("/node/Person.csv", LabelTypes.Person.name());
        createNode.createOnlyIndex(LabelTypes.Person.name(), "id");

        createNode.createNode( "/node/Movie.csv", LabelTypes.Movie.name());
        createNode.createOnlyIndex(LabelTypes.Movie.name(), "id");

        createNode.createNode( "/node/Country.csv", LabelTypes.Country.name());
        createNode.createOnlyIndex(LabelTypes.Country.name(), "id");
    }

    /**
     * 构建 relation
     * @param driver
     */
    public static void createRelation(Driver driver) {
        CreateRelation createRelation = new CreateRelation(driver);

        createRelation.createRelation("/relation/actor.csv", LabelTypes.Movie.name(), LabelTypes.Person.name(), RelTypes.ACTOR_OF.name());

        createRelation.createRelation("/relation/composer.csv", LabelTypes.Movie.name(), LabelTypes.Person.name(), RelTypes.COMPOSER_OF.name());

        createRelation.createRelation("/relation/director.csv", LabelTypes.Movie.name(), LabelTypes.Person.name(), RelTypes.DIRECTOR_OF.name());

        createRelation.createRelation("/relation/district.csv", LabelTypes.Movie.name(), LabelTypes.Country.name(), RelTypes.DISTRICT_OF.name());
    }


}
