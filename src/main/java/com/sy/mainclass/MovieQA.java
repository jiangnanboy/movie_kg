package com.sy.mainclass;

import com.hankcs.hanlp.seg.common.Term;
import com.sy.qa.QuestionAnswer;
import com.sy.qa.QuestionClassification;
import com.sy.qa.QuestionMine;
import com.sy.util.InitNeo4j;
import com.sy.util.InitSparkSession;
import com.sy.util.PropertiesReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.SparkSession;

/**
 * @author YanShi
 * @date 2020/8/28 20:29
 */
public class MovieQA {
    public static void main(String[] args) {

        //对训练数据进行分词并保存，这里使用hanlp进行分词(没有在spark中进行分词是因为使用hanlp时无法序列化)
        if(Files.notExists(Paths.get(PropertiesReader.get("questionClassificatoinSegmentTrain")))) {
            trainDataToSegment(PropertiesReader.get("questionClassificationTrain"), PropertiesReader.get("questionClassificatoinSegmentTrain"));
        }

        //训练
        SparkSession sparkSession = InitSparkSession.getSparkSession();
        sparkSession.sparkContext().setLogLevel("ERROR");
        QuestionClassification classification = new QuestionClassification(sparkSession);
        if(Files.notExists(Paths.get(PropertiesReader.get("modelFile")))) {
            classification.train(PropertiesReader.get("questionClassificatoinSegmentTrain"), PropertiesReader.get("modelFile"));
        }

        //对话预测
        Scanner sc = new Scanner(System.in);
        String question = null;
        System.out.println("小嘉：您好，我是小嘉，请问您想知道些什么呢？");
        while (true) {
            System.out.print("you：");
            question = sc.nextLine();
            if(StringUtils.equals(question, "exit")) {
                break;
            }
            List<Term> listTerm = QuestionMine.extractNer(question);
            StringBuffer sb = new StringBuffer();
            for(Term term:listTerm) {
                if (term.nature.toString().equals("nnt")) {
                    sb.append("nnt").append(" ");
                } else if(term.nature.toString().equals("nm")) {
                    sb.append("nm").append(" ");
                } else  if(term.nature.toString().equals("ng")) {
                    sb.append("ng").append(" ");
                } else {
                    sb.append(term.word).append(" ");
                }
            }
            double label = classification.predict(PropertiesReader.get("modelFile"), sb.toString().trim());
            //System.out.println("预测的类型label为:" + label);
            QuestionAnswer questionAnswer = new QuestionAnswer(InitNeo4j.getDriver());
            StringBuffer resultLine = new StringBuffer();
            List<String> responseResult = questionAnswer.response(label, listTerm);
            //打印结果
            for(String s : responseResult) {
                resultLine.append(s).append(" ");
            }
            System.out.println("小嘉：" + resultLine.toString());
        }
        InitNeo4j.closeDriver();
    }

    /**
     * 对训练数据进行分词并保存
     * @param trainDataPath
     * @param trainDataSavePath
     */
    public static void trainDataToSegment(String trainDataPath, String trainDataSavePath) {
        try(BufferedReader br = Files.newBufferedReader(Paths.get(trainDataPath));
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(trainDataSavePath))) {
            String line = null;
            while ((line=br.readLine())!=null) {
                String[] str = line.split(",");
                String label = str[0];
                String features = QuestionMine.sentenceSegment(str[1]);
                bw.append(label).append(",").append(features);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
