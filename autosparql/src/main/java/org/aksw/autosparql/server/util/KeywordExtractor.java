package org.aksw.autosparql.server.util;

import java.util.List;

public interface KeywordExtractor {
	
	List<String> extractKeywords(String phrase);

}