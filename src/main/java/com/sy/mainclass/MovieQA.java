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
import org.neo4j.driver.v1.Driver;
/**
 * @author YanShi
 * @date 2020/8/28 20:29
 */
public class MovieQA {
    static Driver driver = null;
    public static void main(String[] args) {
        Map<Integer, String> templete = new HashMap<>();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(PropertiesReader.get("questionClassification")))) {
            String line = null;
            while ((line=br.readLine())!=null) {
                String[] str = line.split(":");
                templete.put(Integer.valueOf(str[0]), str[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        QuestionClassification classification = new QuestionClassification(InitSparkSession.getSparkSession());
        if(Files.notExists(Paths.get(PropertiesReader.get("modelFile")))) {
            classification.train(PropertiesReader.get("questionClassificationTrain"), PropertiesReader.get("modelFile"));
        }
        String question = "章子怡演的电影有哪些？";
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

        QuestionAnswer questionAnswer = new QuestionAnswer(driver);
        questionAnswer.response(label, listTerm);
        InitNeo4j.closeDriver();
    }

    static {
        String url = PropertiesReader.get("url");
        String name = PropertiesReader.get("name");
        String password = PropertiesReader.get("password");
        driver = InitNeo4j.initDriver(url, name, password);
    }
}
