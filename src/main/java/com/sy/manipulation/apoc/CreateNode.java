package com.sy.manipulation.apoc;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;

import org.neo4j.driver.v1.summary.ResultSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by YanShi on 2020/7/24 1:10 下午
 */
public class CreateNode {

    private Driver driver = null;

    public CreateNode(Driver driver) {
        this.driver = driver;
    }

    /**
     * 构建node
     * @param file
     * @param nodeLabel
     * @return
     */
    public void createNode(String file, String nodeLabel) {
        System.out.println("start create node of " + nodeLabel);
        try(Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run(" call apoc.periodic.iterate(" +
                    "'call apoc.load.csv(\"" + file + "\",{header:true,sep:\",\",ignore:[\"label\"]}) yield map as row return row'," +
                    "'create(p:" + nodeLabel +") set p=row'," +
                    "{batchSize:1000,iterateList:true, parallel:true}) "));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create node " + nodeLabel + " done!");
    }

    /**
     * 构建索引
     * @param nodeLabel
     * @param property
     */
    public boolean createIndex(String nodeLabel, String property) {
        ResultSummary summary = null;
        try(Session session = driver.session()) {
            summary = session.writeTransaction(tx -> tx.run("create index on :" + nodeLabel+ "( " + property+ ")")).summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create index on " + nodeLabel + "." +property + " done!");
        return summary.counters().indexesAdded() == 1;
    }

    /**
     * 构建唯一索引
     * @param nodeLabel
     * @param property
     * @return
     */
    public boolean createOnlyIndex(String nodeLabel, String property) {
        ResultSummary summary = null;
        try(Session session = driver.session()) {
            summary = session.writeTransaction(tx -> tx.run("create constraint on (s:" + nodeLabel + ") assert s." + property + " is unique")).summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("create only index on " + nodeLabel + "." +property + " done!");
        return summary.counters().constraintsAdded() == 1;
    }

}
