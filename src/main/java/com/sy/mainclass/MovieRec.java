package com.sy.mainclass;

import com.sy.reco.CBF;
import com.sy.util.InitNeo4j;

/**
 * @author YanShi
 * @date 2020/9/24 22:41
 */
public class MovieRec {
    public static void main(String[] args) {
        CBF cbf = new CBF(InitNeo4j.getDriver());
        cbf.recommender("建国大业", 10).stream().forEach(title -> System.out.println(title));
        InitNeo4j.closeDriver();
    }
}
