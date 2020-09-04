package com.sy.manipulation.basic_operation;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;

import java.util.List;

/**
 * Created by YanShi on 2020/9/4 22:27
 */
public class GraphCount {

    Driver driver = null;
    public GraphCount(Driver driver) {
        this.driver = driver;
    }


    /**
     * 统计label为nodeLabel的节点数量
     * @param nodeLabel
     */
    public int countNode(String nodeLabel) {
        int nodeCount =0;
        try(Session session = driver.session()) {
            List<Record> listRecord = session.readTransaction(tx -> tx.run("match(:" + nodeLabel + ") return count(*)")).list();
            nodeCount = listRecord.get(0).get(0).asInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeCount;
    }

}
