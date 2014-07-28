package org.aksw.autosparql.commons.nlp.ner;

import org.aksw.autosparql.commons.nlp.ner.LTPNamedEntityTagger;
import org.junit.Test;

public class LTPNamedEntityTaggerTest
{

	@Test public void testLTPPartOfSpeechTagger()
	{
		String s = LTPNamedEntityTagger.INSTANCE.getAnnotatedSentence("北京和上海是中国的政治和经济中心.");
        System.out.println(s);
	}

}