package com.sy.manipulation.basic_operation;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.summary.ResultSummary;

/**
 * Created by YanShi on 2020/9/4 22:27
 */
public class GraphDelete {

    Driver driver = null;
    public GraphDelete(Driver driver) {
        this.driver = driver;
    }

    /**
     * 删除label为nodeLabel的所有节点(无关系)
     * @param nodeLabel
     */
    public boolean deleteAllNode(String nodeLabel) {
        ResultSummary summary = null;
        try(Session session = driver.session()) {
            summary = session.writeTransaction(tx -> tx.run("match(n) delete n")).summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary.counters().nodesDeleted() == 1;
    }

    /**
     * 删除label为nodeLabel的所有节点以及与之相关的所有关系
     * @param nodeLabel
     * @return
     */
    public boolean deleteAllRelation(String nodeLabel) {
        ResultSummary summary = null;
        try(Session session = driver.session()) {
            summary = session.writeTransaction(tx -> tx.run("match (n:"+nodeLabel+")-[r]-() delete r,n")).summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (summary.counters().nodesDeleted() == 1) || (summary.counters().relationshipsDeleted() == 1);
    }

    /**
     * 删除index
     * @param nodeLabel
     * @param property
     */
    public boolean deleteIndex(String nodeLabel, String property) {
        ResultSummary summary =null;
        try(Session session = driver.session()) {
            summary = session.writeTransaction(tx -> tx.run("drop index on:" + nodeLabel + "(" + property + ")")).summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary.counters().indexesRemoved() == 1;
    }

    /**
     * 删除唯一索引
     * @param nodeLabel
     * @param property
     * @return
     */
    public boolean deleteOnlyIndex(String nodeLabel, String property) {
        ResultSummary summary =null;
        try(Session session = driver.session()) {
            summary = session.writeTransaction(tx -> tx.run("drop constraint on (s:" + nodeLabel + ") assert s." + property + " is unique")).summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summary.counters().constraintsRemoved() == 1;
    }


}
