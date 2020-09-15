package com.sy.qa;

import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * @author YanShi
 * @date 2020/8/28 20:29
 */
public class QuestionAnswer {
    public static void main(String[] args) {
        QuestionMine questionMine = new QuestionMine();
        String question = "章子怡的卧虎藏龙中还有哪些演员？";
        List<Term> listTerm = questionMine.extractNer(question);
        System.out.println(listTerm);
        for(Term term:listTerm) {
            System.out.println(term.word+" : "+term.nature.toString());
        }
    }

    public void response(String question) {

    }

}
