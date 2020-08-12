package com.sy.base.abs;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.neo4j.driver.v1.Driver;

import java.io.BufferedReader;
import java.util.Iterator;

/**
 * Created by YanShi on 2020/7/31 10:34 上午
 */
public abstract class AbsCSVRead {
    public Driver driver = null;
    public AbsCSVRead(Driver drive){this.driver = drive;}

    /**
     * read csv file
     * @param br
     * @param clazz
     * @param <T>
     * @return
     */
    public  <T> Iterator<T> readCSV(BufferedReader br, Class<T> clazz) {
        Iterator<T> iterator = null;
        HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(clazz);
        CsvToBean<T> build = new CsvToBeanBuilder<T>(br)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(',')
                    .withQuoteChar('\"')
                    .build();
        iterator = build.iterator();
        return iterator;
    }

}
