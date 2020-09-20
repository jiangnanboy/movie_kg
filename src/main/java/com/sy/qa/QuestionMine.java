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
    private static Segment segment = null;
    static {
        segment = HanLP.newSegment().enableCustomDictionaryForcing(true);//强制使用自定义词典
    }

    /**
     * 抽取人名和电影名
     * @param question
     * @return
     */
    public static List<Term> extractNer(String question) {
        List<Term> listTerm = CoreStopWordDictionary.apply(segment.seg(question));
        return listTerm;
    }

    /**
     * 分词
     * @param text
     * @return
     */
    public static String sentenceSegment(String text) {
        StringBuffer sb = new StringBuffer();
        for(Term term : segment.seg(text)) {
            sb.append(term.word).append(" ");
        }
        return sb.toString().trim();
    }

}


