package org.aksw.autosparql.commons.nlp.pos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LTPPartOfSpeechTaggerTest
{

	@Test public void testLTPPartOfSpeechTagger()
	{
		String s = LTPPartOfSpeechTagger.INSTANCE.tag("列出 所有 丹·布朗 的 书.");
        System.out.println(s);
	}

}