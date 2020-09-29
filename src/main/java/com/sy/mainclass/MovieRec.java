package com.sy.mainclass;

import com.sy.reco.Recommender;
import com.sy.util.InitNeo4j;

/**
 * @author YanShi
 * @date 2020/9/24 22:41
 */
public class MovieRec {
    public static void main(String[] args) {
        Recommender rec = new Recommender(InitNeo4j.getDriver());
        rec.recCBF1("泰坦尼克号", 10).stream().forEach(title -> System.out.println(title));
        System.out.println("------------------");
        rec.recCBF2("泰坦尼克号", 10).stream().forEach(title -> System.out.println(title));
        InitNeo4j.closeDriver();
    }
}
