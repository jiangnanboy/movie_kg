package com.sy.qa;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author YanShi
 * @date 2020/9/7 22:03
 */
public class QuestionMine {

    /**
     * 抽取人名和电影名
     * @param question
     * @return
     */
    public  List<Term> extractNer(String question) {
        Segment segment = HanLP.newSegment().enableCustomDictionaryForcing(true);//强制使用自定义词典
        List<Term> listTerm = CoreStopWordDictionary.apply(segment.seg(question));
        return listTerm;
    }

    /**
     * 依存关系分析
     * @param question
     */
    public void sentDependency(String question) {
        CoNLLSentence sentence = HanLP.parseDependency(question);
        System.out.println(sentence);
        System.out.println("----------");
        CoNLLWord[] wordArray = sentence.getWordArray();
        for (int i =0; i < wordArray.length; i++)
        {
            CoNLLWord word = wordArray[i];
            System.out.println(word.LEMMA+";"+word.POSTAG+";"+word.HEAD.ID+";"+word.HEAD+";"+word.DEPREL);

        }
        System.out.println("----------");
        System.out.println(HanLP.segment(question));
    }
}


