package com.sy.reco;

import com.sy.util.InitNeo4j;
import org.apache.spark.sql.sources.In;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YanShi
 * @date 2020/9/24 21:54
 */
public class CBF {

    Driver driver = null;
    public CBF(Driver driver) {
        this.driver = driver;
    }

    /**
     * 推荐相似电影，根据相同的演员或导演，返回top-n评分的电影
     * @param movieTitle
     * @param topn
     * @return
     */
    public List<String> recommender(String movieTitle, int topn) {
        List<String> result = new ArrayList<>();
        List<Record> listRecord = null;
        try(Session session = driver.session()) {
            listRecord = session.readTransaction(tx -> tx.run("MATCH path=(m:Movie)-[:ACTOR_OF|:DIRECTOR_OF]->(p:Person)<-[:ACTOR_OF|:DIRECTOR_OF]-(rec:Movie) " +
                    "where m.title = {title} with distinct rec order by rec.rate desc return rec.title as title limit {topn}", Values.parameters("title", movieTitle, "topn", topn))).list();
        }
        for(Record record : listRecord){
            result.add(record.get("title").toString());
        }
        return result;
    }
}












































