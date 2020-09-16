package com.sy.qa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import javax.sql.DataSource;


/**
 * @author YanShi
 * @date 2020/9/9 22:06
 */
public class QuestionClassification {

    SparkSession session = null;
    Segment segment = null;
    public QuestionClassification(SparkSession session) {
        this.session = session;
        this.segment = HanLP.newSegment().enableCustomDictionaryForcing(true);//强制使用自定义词典
    }

    /**
     * 训练
     * @param trainFile
     * @param modelFile
     */
    public void train(String trainFile, String modelFile) {

        StructType schema = new StructType(new StructField[] {
                new StructField("label", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
        });

        //1
        JavaRDD<Row> rowJavaRDD = session.read().textFile(trainFile).toJavaRDD().mapPartitions(new FlatMapFunction<Iterator<String>, Row>() {
            @Override
            public Iterator<Row> call(Iterator<String> input) throws Exception {
                List<Row> listRow = new ArrayList<>();
                while(input.hasNext()) {
                    String[] line = input.next().split(",");
                    int label = Integer.parseInt(line[0]);
                    String feature = line[1];
                    listRow.add(RowFactory.create(label, sentenceSegment(feature)));
                }
                return listRow.iterator();
            }
        });
        Dataset<Row> dataset = session.createDataFrame(rowJavaRDD, schema);

        //2
        /*Encoder<Row> rowEncoder = Encoders.javaSerialization(Row.class);
        Dataset<Row> dataset = session.read().textFile(trainFile).mapPartitions(new MapPartitionsFunction<String, Row>() {
            @Override
            public Iterator<Row> call(Iterator<String> input) throws Exception {
                List<Row> listRow = new ArrayList<>();
                while(input.hasNext()) {
                    String[] line = input.next().split(",");
                    int label = Integer.parseInt(line[0]);
                    String feature = line[1];
                    StringBuffer sb = new StringBuffer();
                    for(Term term : segment.seg(feature)) {
                        sb.append(term.word).append(" ");
                    }
                    listRow.add(RowFactory.create(label, sb.toString()));
                }
                return listRow.iterator();
            }
        }, rowEncoder);*/

        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("sentence")
                .setOutputCol("words");
        HashingTF hashingTF = new HashingTF()
                .setNumFeatures(1000)
                .setInputCol(tokenizer.getOutputCol())
                .setOutputCol("rowfeatures");
        IDF idf = new IDF()
                .setInputCol(hashingTF.getOutputCol())
                .setOutputCol("features");
        NaiveBayes nb = new NaiveBayes()
                .setSmoothing(0.001);
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] {tokenizer, hashingTF, idf, nb});
        PipelineModel model = pipeline.fit(dataset);
        try {
            model.save(modelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预测
     * @param modelFile
     * @param text
     * @return
     */
    public int predict(String modelFile, String text) {
        StructType schema = new StructType(new StructField[] {
                new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
        });
        List<Row> predictRow = new ArrayList<>();
        predictRow.add(RowFactory.create(sentenceSegment(text)));
        Dataset<Row> prediction = session.createDataFrame(predictRow, schema);
        PipelineModel model = PipelineModel.load(modelFile);
        Dataset<Row> predictions = model.transform(prediction);
        return predictions.select("prediction").collectAsList().get(0).getInt(0);
    }

    /**
     * 分词
     * @param text
     * @return
     */
    public String sentenceSegment(String text) {
        StringBuffer sb = new StringBuffer();
        for(Term term : segment.seg(text)) {
            sb.append(term.word).append(" ");
        }
        return sb.toString().trim();
    }
}
