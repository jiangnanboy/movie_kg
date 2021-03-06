package com.sy.manipulation.apoc;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.summary.ResultSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YanShi on 2020/7/24 1:10 下午
 */
public class CreateRelation {

    private Driver driver = null;

    public CreateRelation(Driver driver) {this.driver = driver;}

    /**
     * 构建关系
     * @param file
     * @param startNodeLabel
     * @param endNodeLabel
     * @param relType
     * @return
     */
    public boolean createRelation(String file, String startNodeLabel, String endNodeLabel, String relType) {
        ResultSummary summary = null;
        System.out.println("start create relation of " + relType + "...");
        try (Session session = driver.session()){
            summary = session.writeTransaction(tx -> tx.run("call apoc.periodic.iterate(" +
                    "'call apoc.load.csv(\""+ file + "\",{header:true,sep:\",\",ignore:[\"type\"]}) yield map as row match (start:" + startNodeLabel + "{id:row.startId}), (end:" + endNodeLabel + "{id:row.endId}) return start,end'," +
                    "'create (start)-[:" + relType + "]->(end)'," +
                    "{batchSize:1000,iterateList:true, parallel:false});")).summary(); //这里注意parallel为false，保证在创建节点之间关系时不会产生死锁问题
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("create relation of " + relType + " done!");
        return summary.counters().relationshipsCreated() == 1;
    }


}
