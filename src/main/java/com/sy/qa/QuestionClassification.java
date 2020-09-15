package com.sy.qa;

import java.util.Arrays;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * @author YanShi
 * @date 2020/9/9 22:06
 */
public class QuestionClassification {

    public void train(String trainFile, String modelFile) {

    }

    public void predict(String modelFile, String text) {

    }
}
