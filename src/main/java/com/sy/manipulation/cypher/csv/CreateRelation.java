package com.sy.manipulation.cypher.csv;

import com.sy.base.abs.AbsCSVRead;

import org.neo4j.driver.v1.*;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.sy.relation.Actor;
import com.sy.relation.Composer;
import com.sy.relation.Director;
import com.sy.relation.District;

/**
 * Created by YanShi on 2020/7/24 1:10 下午
 */
public class CreateRelation extends AbsCSVRead {

    public CreateRelation(Driver driver) {super(driver);}

    /**
     * 创建 the relatin of actor
     * @param csvFile
     */
    public void createActorRel(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath));Session session = driver.session();){
            Iterator<Actor> iterator = readCSV(br, Actor.class);
            while (iterator.hasNext()) {
                Actor actor = iterator.next();
                session.writeTransaction(tx -> tx.run("match (m:Movie), (p:Person) where m.id = " +
                        "\"" + actor.startID + "\" and p.id = \"" + actor.endID + "\" merge (m)-[:actor]->(p)"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create relationship of actor done!");
    }

    /**
     * 创建 the relation of composer
     * @param csvFile
     */
    public void createComposerRel(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath));Session session = driver.session();){
            Iterator<Composer> iterator = readCSV(br, Composer.class);
            while (iterator.hasNext()) {
                Composer composer = iterator.next();
                session.writeTransaction(tx -> tx.run("match (m:Movie), (p:Person) where m.id = " +
                        "\"" + composer.startID + "\" and p.id = \"" + composer.endID + "\" merge (m)-[:composer]->(p)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create relationship of composer done!");
    }

    /**
     * 创建 the relation of director
     * @param csvFile
     */
    public void createDirectorRel(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath));Session session = driver.session();){
            Iterator<Director> iterator = readCSV(br, Director.class);
            while (iterator.hasNext()) {
                Director director = iterator.next();
                session.writeTransaction(tx -> tx.run("match (m:Movie), (p:Person) where m.id = " +
                        "\"" + director.startID + "\" and p.id = \"" + director.endID +"\" merge (m)-[:director]->(p)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create relationship of director done!");
    }

    /**
     * 创建 the relation of district
     * @param csvFile
     */
    public void createDistrictRel(String csvFile) {
        Path fPath = Paths.get(csvFile);
        try(BufferedReader br = Files.newBufferedReader((fPath));Session session = driver.session();){
            Iterator<District> iterator = readCSV(br, District.class);
            while (iterator.hasNext()) {
                District district = iterator.next();
                session.writeTransaction(tx -> tx.run("match (m:Movie), (c:Country)" +
                        "where m.id = \"" + district.startID + "\" AND c.id = \"" + district.endID + "\" merge (m)-[:district]->(c)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create relationship of district done!");
    }

}

