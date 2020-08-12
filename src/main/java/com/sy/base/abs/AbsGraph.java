package com.sy.base.abs;

import org.neo4j.driver.v1.Driver;

/**
 * @Author Shi Yan
 * @Date 2020/8/7 13:07
 */
public abstract  class AbsGraph {
    public Driver driver = null;
    public AbsGraph(Driver driver) {
        this.driver = driver;
    }
}
