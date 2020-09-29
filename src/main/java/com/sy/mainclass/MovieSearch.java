package com.sy.mainclass;

import com.sy.search.Search;
import com.sy.util.InitNeo4j;

/**
 * @author YanShi
 * @date 2020/9/29 21:39
 */
public class MovieSearch {
    public static void main(String[] args) {
        Search search = new Search(InitNeo4j.getDriver());
        search.getMostRatedScoreMovie(String.valueOf(7.5), "科幻").stream().forEach(movie -> System.out.println(movie));
        InitNeo4j.closeDriver();
    }
}
