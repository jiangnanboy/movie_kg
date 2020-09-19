package com.sy.mainclass;

import com.hankcs.hanlp.seg.common.Term;
import com.sy.qa.QuestionAnswer;
import com.sy.qa.QuestionClassification;
import com.sy.qa.QuestionMine;
import com.sy.util.InitNeo4j;
import com.sy.util.InitSparkSession;
import com.sy.util.PropertiesReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.SparkSession;
import org.neo4j.driver.v1.Driver;
/**
 * @author YanShi
 * @date 2020/8/28 20:29
 */
public class MovieQA {
    public static void main(String[] args) {
        /*Map<Integer, String> templete = new HashMap<>();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(PropertiesReader.get("questionClassification")))) {
            String line = null;
            while ((line=br.readLine())!=null) {
                String[] str = line.split(":");
                templete.put(Integer.valueOf(str[0]), str[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        SparkSession sparkSession = InitSparkSession.getSparkSession();
        sparkSession.sparkContext().setLogLevel("ERROR");
        QuestionClassification classification = new QuestionClassification(sparkSession);
        if(Files.notExists(Paths.get(PropertiesReader.get("modelFile")))) {
            classification.train(PropertiesReader.get("questionClassificationTrain"), PropertiesReader.get("modelFile"));
        }

        Scanner sc = new Scanner(System.in);
        String question = null;
        while (true) {
            System.out.println("请输入一个问句：");
            question = sc.nextLine();
            if(StringUtils.equals(question, "exit")) {
                break;
            }
            QuestionMine questionMine = new QuestionMine();
            List<Term> listTerm = questionMine.extractNer(question);
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
            int label = classification.predict(PropertiesReader.get("modelFile"), sb.toString().trim());

            QuestionAnswer questionAnswer = new QuestionAnswer(InitNeo4j.getDriver());
            questionAnswer.response(label, listTerm);
        }
        InitNeo4j.closeDriver();
    }

}
