package org.aksw.autosparql.tbsl.algorithm.learning;

import org.aksw.autosparql.tbsl.algorithm.learning.AGISTISEntityDisambiguation;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.ArrayList;

public class AGISTISEntityDisambiguationTest
{
	@Test public void testAGISTISEntityDisambiguation()
	{
        try{
            ArrayList<Pair<String, String>> links = AGISTISEntityDisambiguation.getCandidateEntities("<entity>北京</entity> 和 <entity>上海</entity> 是 <entity>中国</entity> 的政治和经济中心.");
            for(Pair<String, String> pair: links){
                System.out.println("uri:"+pair.getLeft() + "\tlabel:" + pair.getRight());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(s);
	}

}